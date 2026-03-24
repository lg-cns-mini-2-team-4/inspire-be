// package com.example.certificate_service.service;

// import com.example.certificate_service.client.PublicApiClient;
// import com.example.certificate_service.dao.CertificateRepository;
// import com.example.certificate_service.dao.ExamRepository;
// import com.example.certificate_service.domain.dto.CertificateDetailResponseDTO;
// import com.example.certificate_service.domain.entity.CertificateEntity;
// import com.example.certificate_service.domain.entity.ExamEntity;
// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.dataformat.xml.XmlMapper;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import com.example.certificate_service.domain.dto.CertificateWithSchedulesDTO;
// import com.example.certificate_service.domain.dto.ExamDetailResponseDTO;

// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
// import com.fasterxml.jackson.databind.ObjectMapper;

// import java.time.LocalDate;
// import java.time.format.DateTimeFormatter;
// import java.util.List;
// import java.util.stream.Collectors;

// @Slf4j
// @Service
// @RequiredArgsConstructor
// public class CertificateService {

//     private final CertificateRepository certificateRepository;
//     private final ExamRepository examRepository;
//     private final PublicApiClient publicApiClient;
//     private final XmlMapper xmlMapper;
//     private final ObjectMapper objectMapper;

//     private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

//     // 날짜 파싱 (빈 값 처리 포함)
//     private LocalDate parseDate(String dateStr) {
//         if (dateStr == null || dateStr.isBlank()) return null;
//         try {
//             return LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
//         } catch (Exception e) {
//             return null;
//         }
//     }

//     // 자격증 상세 조회
//     public CertificateDetailResponseDTO getCertificateDetail(String itemCode) {
//         CertificateEntity entity = certificateRepository.findById(itemCode)
//                 .orElseThrow(() -> new RuntimeException("자격증을 찾을 수 없습니다: " + itemCode));
//         return CertificateDetailResponseDTO.from(entity);
//     }

//     // 자격증 + 시험일정 통합 조회
//     public CertificateWithSchedulesDTO getCertificateWithSchedules(String itemCode) {
//         CertificateEntity cert = certificateRepository.findById(itemCode)
//                 .orElseThrow(() -> new RuntimeException("자격증을 찾을 수 없습니다: " + itemCode));
//         List<ExamEntity> schedules = examRepository.findByCertificate_ItemCode(itemCode);
//         return CertificateWithSchedulesDTO.from(cert, schedules);
//     }

//     // 자격증 종목 동기화 (XML 파싱)
//     @Transactional
//     public void syncCertificates() {
//         String xml = publicApiClient.fetchCertificateList();
//         if (xml == null) {
//             log.warn("API 응답이 null입니다.");
//             return;
//         }

//         try {
//             // xml.getBytes() 대신 xml 문자열을 직접 파싱하여 인코딩 깨짐 방지
//             JsonNode root = xmlMapper.readTree(xml);
//             JsonNode items = root.path("body").path("items").path("item");

//             if (items.isObject()) {
//                 // 결과가 1개일 때
//                 saveCertificateNode(items);
//             } else if (items.isArray()) {
//                 // 결과가 여러 개(배열)일 때
//                 for (JsonNode item : items) {
//                     saveCertificateNode(item);
//                 }
//             } else {
//                 // 배열도 객체도 아니면 API 에러일 확률이 높음. 실제 응답 내용 출력
//                 log.error("API 응답 형식이 예상과 다릅니다. 실제 응답 내용: {}", xml);
//             }
//             log.info("자격증 종목 동기화 완료");
//         } catch (Exception e) {
//             log.error("자격증 종목 파싱 실패: {}", e.getMessage(), e);
//         }
//     }

//     // 자격증 종목 저장 헬퍼 메서드 (가독성을 위해 분리)
//     private void saveCertificateNode(JsonNode item) {
//         CertificateEntity entity = CertificateEntity.builder()
//                 .itemCode(item.path("jmcd").asText())
//                 .itemName(item.path("jmfldnm").asText())
//                 .certTypeCode(item.path("qualgbcd").asText())
//                 .certTypeName(item.path("qualgbnm").asText())
//                 .seriesCode(item.path("seriescd").asText())
//                 .seriesName(item.path("seriesnm").asText())
//                 .largeFieldCode(item.path("obligfldcd").asText())
//                 .largeFieldName(item.path("obligfldnm").asText())
//                 .build();
//         certificateRepository.save(entity);
//     }



//     // 시험 일정 동기화 (상위 10개 한정 및 JSON 출력 버전)
//     @Transactional
//     public void syncSchedules(String year) {
//         // stream()과 limit(10)을 사용하여 상위 10개만 가져옵니다.
//         List<CertificateEntity> certificates = certificateRepository.findAll()
//                 .stream()
//                 .limit(10)
//                 .toList();

//         for (CertificateEntity certificate : certificates) {
//             String json = publicApiClient.fetchScheduleList(
//                 year,
//                 certificate.getItemCode(),
//                 certificate.getCertTypeCode()
//             );

//             // 매 시도마다 받아온 JSON 로그 출력
//             log.info("자격증 코드: {}, 종목 코드: {}, JSON 결과: {}", 
//                      certificate.getCertTypeCode(), certificate.getItemCode(), json);

//             if (json == null || json.isEmpty()) continue; 

//             try {
//                 JsonNode root = objectMapper.readTree(json);
//                 JsonNode items = root.path("body").path("items");

//                 if (items.isObject()) {
//                     saveSchedule(items, certificate);
//                 } else if (items.isArray()) {
//                     for (JsonNode item : items) {
//                         saveSchedule(item, certificate);
//                     }
//                 }
//             } catch (Exception e) {
//                 log.error("시험일정 파싱 실패 ({}년 {}): {}", year, certificate.getItemCode(), e.getMessage());
//             }
//         }
//         log.info("{}년 상위 10개 시험일정 동기화 완료", year);
//     }

//     private void saveSchedule(JsonNode item, CertificateEntity certificate) {
//         String implYear = item.path("implYy").asText();
//         Integer implSeq = item.path("implSeq").asInt();

//         // 중복 저장 방지
//         if (examRepository.existsByImplYearAndImplSeqAndCertificate(implYear, implSeq, certificate)) {
//             return;
//         }
//         ExamEntity entity = ExamEntity.builder()
//                 .certificate(certificate)
//                 .implYear(item.path("implYy").asText())
//                 .implSeq(item.path("implSeq").asInt())
//                 .description(item.path("description").asText())
//                 .writtenRegStart(parseDate(item.path("docRegStartDt").asText()))
//                 .writtenRegEnd(parseDate(item.path("docRegEndDt").asText()))
//                 .writtenExamStart(parseDate(item.path("docExamStartDt").asText()))
//                 .writtenExamEnd(parseDate(item.path("docExamEndDt").asText()))
//                 .writtenPassDate(parseDate(item.path("docPassDt").asText()))
//                 .practicalRegStart(parseDate(item.path("pracRegStartDt").asText()))
//                 .practicalRegEnd(parseDate(item.path("pracRegEndDt").asText()))
//                 .practicalExamStart(parseDate(item.path("pracExamStartDt").asText()))
//                 .practicalExamEnd(parseDate(item.path("pracExamEndDt").asText()))
//                 .practicalPassDate(parseDate(item.path("pracPassDt").asText()))
//                 .build();
//         scheduleRepository.save(entity);
//     }
// }

package com.inspire.certificate_service.service;

import com.inspire.certificate_service.client.PublicApiClient;
import com.inspire.certificate_service.dao.CertificateRepository;
import com.inspire.certificate_service.dao.ExamRepository;
import com.inspire.certificate_service.domain.dto.ExamDetailResponseDTO;
import com.inspire.certificate_service.domain.entity.CertificateEntity;
import com.inspire.certificate_service.domain.entity.ExamEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final ExamRepository examRepository;
    private final PublicApiClient publicApiClient;
    private final XmlMapper xmlMapper;
    private final ObjectMapper objectMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");



    // 1. 날짜 파싱 헬퍼 (기존에 정의했던 것)
    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank() || dateStr.equals("null")) return null;
        try {
            return LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 자격증 상세 + 회차별 조립된 일정 조회
     */
    public ExamDetailResponseDTO getCertificateDetailWithSchedules(String itemCode) {
        CertificateEntity cert = certificateRepository.findById(itemCode)
                .orElseThrow(() -> new RuntimeException("자격증을 찾을 수 없습니다: " + itemCode));
        
        List<ExamEntity> allRows = examRepository.findByCertificate_ItemCode(itemCode);
        return ExamDetailResponseDTO.from(cert, allRows);
    }

    /**
     * 1. 자격증 종목 동기화 (XML 파싱)
     */
    @Transactional
    public void syncCertificates() {
        String xml = publicApiClient.fetchCertificateList();
        if (xml == null) return;

        try {
            JsonNode root = xmlMapper.readTree(xml);
            JsonNode items = root.path("body").path("items").path("item");

            if (items.isObject()) {
                saveCertificateNode(items);
            } else if (items.isArray()) {
                for (JsonNode item : items) {
                    saveCertificateNode(item);
                }
            }
            log.info("자격증 종목 동기화 완료");
        } catch (Exception e) {
            log.error("자격증 종목 동기화 실패: {}", e.getMessage());
        }
    }

    /**
     * 2. 시험 일정 동기화 (행 단위 분리 저장)
     */
    @Transactional
    public void syncSchedules(String year) {
        // 테스트를 위해 상위 10개만 진행
        List<CertificateEntity> certificates = certificateRepository.findAll()
                .stream().limit(10).toList();

        for (CertificateEntity certificate : certificates) {
            String json = publicApiClient.fetchScheduleList(year, certificate.getItemCode(), certificate.getCertTypeCode());
            if (json == null || json.isEmpty()) continue;

            try {
                JsonNode root = objectMapper.readTree(json);
                JsonNode items = root.path("body").path("items");

                if (items.isObject()) {
                    processAndSaveExamRows(items, certificate);
                } else if (items.isArray()) {
                    for (JsonNode item : items) {
                        processAndSaveExamRows(item, certificate);
                    }
                }
            } catch (Exception e) {
                log.error("일정 파싱 실패 ({}): {}", certificate.getItemCode(), e.getMessage());
            }
        }
    }

    /**
     * API 한 줄의 데이터를 우리 DB의 여러 행(Type별)으로 쪼개서 저장
     */
    private void processAndSaveExamRows(JsonNode item, CertificateEntity certificate) {
        String year = item.path("implYy").asText();
        Integer seq = item.path("implSeq").asInt();
        String desc = item.path("description").asText();

        // 1. 필기 접수 (WR)
        saveExamRow(certificate, year, seq, "WR", 
                parseDate(item.path("docRegStartDt").asText()), 
                parseDate(item.path("docRegEndDt").asText()), desc);

        // 2. 필기 시험 (WE)
        saveExamRow(certificate, year, seq, "WE", 
                parseDate(item.path("docExamStartDt").asText()), 
                parseDate(item.path("docExamEndDt").asText()), desc);

        // 3. 필기 합격발표 (WP)
        saveExamRow(certificate, year, seq, "WP", 
                null, parseDate(item.path("docPassDt").asText()), desc);

        // 4. 실기 접수 (PR)
        saveExamRow(certificate, year, seq, "PR", 
                parseDate(item.path("pracRegStartDt").asText()), 
                parseDate(item.path("pracRegEndDt").asText()), desc);

        // 5. 실기 시험 (PE)
        saveExamRow(certificate, year, seq, "PE", 
                parseDate(item.path("pracExamStartDt").asText()), 
                parseDate(item.path("pracExamEndDt").asText()), desc);

        // 6. 최종 합격발표 (PD)
        saveExamRow(certificate, year, seq, "PD", 
                null, parseDate(item.path("pracPassDt").asText()), desc);
    }

    private void saveExamRow(CertificateEntity cert, String year, Integer seq, String type, 
                             LocalDate start, LocalDate end, String desc) {
        if (end == null) return; // 종료일(발표일)이 없으면 저장하지 않음

        // 중복 체크: 종목 + 년도 + 회차 + 타입이 이미 있는지 확인
        if (examRepository.existsByCertificateAndImplYearAndImplSeqAndType(cert, year, seq, type)) {
            return;
        }

        ExamEntity exam = ExamEntity.builder()
                .certificate(cert)
                .implYear(year)
                .implSeq(seq)
                .type(type)
                .startDate(start)
                .endDate(end)
                .description(desc)
                .build();
        
        examRepository.save(exam);
    }

    private void saveCertificateNode(JsonNode item) {
        // 1. 우선 값을 가져오기 (공백 제거 포함)
        String fldCode = item.path("obligfldcd").asText().trim();
        String fldName = item.path("obligfldnm").asText().trim();

        // 2. 공통 필드 먼저 빌더로 생성
        CertificateEntity.CertificateEntityBuilder builder = CertificateEntity.builder()
                .itemCode(item.path("jmcd").asText())
                .itemName(item.path("jmfldnm").asText())
                .certTypeCode(item.path("qualgbcd").asText())
                .certTypeName(item.path("qualgbnm").asText())
                .seriesCode(item.path("seriescd").asText())
                .seriesName(item.path("seriesnm").asText());

        // 3. [핵심] 값이 비어있지 않을 때만 세팅!
        // 이렇게 하면 빈 값("")일 때는 세팅을 생략하게 되고, 
        // 결과적으로 엔티티에 설정된 @Builder.Default("00", "기타")가 살아남습니다.
        if (!fldCode.isEmpty()) {
            builder.largeFieldCode(fldCode);
        }
        
        if (!fldName.isEmpty()) {
            builder.largeFieldName(fldName);
        }

        certificateRepository.save(builder.build());
    }
}