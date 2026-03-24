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

import org.springframework.cglib.core.Local;
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
        // saveExamRow(certificate, year, seq, "WP", 
        //         null, parseDate(item.path("docPassDt").asText()), desc);
        LocalDate docPassDate = parseDate(item.path("docPassDt").asText());
        saveExamRow(certificate, year, seq, "WP", 
                docPassDate, docPassDate, desc);

        // 4. 실기 접수 (PR)
        saveExamRow(certificate, year, seq, "PR", 
                parseDate(item.path("pracRegStartDt").asText()), 
                parseDate(item.path("pracRegEndDt").asText()), desc);

        // 5. 실기 시험 (PE)
        saveExamRow(certificate, year, seq, "PE", 
                parseDate(item.path("pracExamStartDt").asText()), 
                parseDate(item.path("pracExamEndDt").asText()), desc);

        // 6. 최종 합격발표 (PD)
        // saveExamRow(certificate, year, seq, "PD", 
        //         null, parseDate(item.path("pracPassDt").asText()), desc);
        LocalDate pracPassDate = parseDate(item.path("pracPassDt").asText());
        saveExamRow(certificate, year, seq, "PD", 
                pracPassDate, pracPassDate, desc);
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