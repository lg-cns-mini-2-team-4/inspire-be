package com.example.certificate_service.service;

import com.example.certificate_service.client.PublicApiClient;
import com.example.certificate_service.dao.CertificateRepository;
import com.example.certificate_service.dao.ScheduleRepository;
import com.example.certificate_service.domain.dto.CertificateDetailResponseDTO;
import com.example.certificate_service.domain.entity.CertificateEntity;
import com.example.certificate_service.domain.entity.ScheduleEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.certificate_service.domain.dto.CertificateWithSchedulesDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.certificate_service.domain.dto.CertificateWithSchedulesDTO;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final ScheduleRepository scheduleRepository;
    private final PublicApiClient publicApiClient;
    private final XmlMapper xmlMapper;
    private final ObjectMapper objectMapper;  // 추가

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    // 날짜 파싱 (빈 값 처리 포함)
    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return null;
        try {
            return LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    // 자격증 상세 조회
    public CertificateDetailResponseDTO getCertificateDetail(String itemCode) {
        CertificateEntity entity = certificateRepository.findById(itemCode)
                .orElseThrow(() -> new RuntimeException("자격증을 찾을 수 없습니다: " + itemCode));
        return CertificateDetailResponseDTO.from(entity);
    }

    // 자격증 + 시험일정 통합 조회
    public CertificateWithSchedulesDTO getCertificateWithSchedules(String itemCode) {
    CertificateEntity cert = certificateRepository.findById(itemCode)
            .orElseThrow(() -> new RuntimeException("자격증을 찾을 수 없습니다: " + itemCode));
    List<ScheduleEntity> schedules = scheduleRepository.findByCertificate_ItemCode(itemCode);
    return CertificateWithSchedulesDTO.from(cert, schedules);
    }

    // 자격증 종목 동기화 (XML 파싱)
    @Transactional
    public void syncCertificates() {
        String xml = publicApiClient.fetchCertificateList();
        if (xml == null) return;

        try {
            JsonNode root = xmlMapper.readTree(xml.getBytes());
            JsonNode items = root.path("body").path("items").path("item");

            if (items.isArray()) {
                for (JsonNode item : items) {
                    CertificateEntity entity = CertificateEntity.builder()
                            .itemCode(item.path("jmcd").asText())
                            .itemName(item.path("jmfldnm").asText())
                            .certTypeCode(item.path("qualgbcd").asText())
                            .certTypeName(item.path("qualgbnm").asText())
                            .seriesCode(item.path("seriescd").asText())
                            .seriesName(item.path("seriesnm").asText())
                            .largeFieldCode(item.path("obligfldcd").asText())
                            .largeFieldName(item.path("obligfldnm").asText())
                            .mediumFieldCode(item.path("mdobligfldcd").asText())
                            .mediumFieldName(item.path("mdobligfldnm").asText())
                            .build();
                    certificateRepository.save(entity);
                }
            }
            log.info("자격증 종목 동기화 완료");
        } catch (Exception e) {
            log.error("자격증 종목 파싱 실패: {}", e.getMessage());
        }
    }

    // 시험 일정 동기화 
    @Transactional
    public void syncSchedules(String year) {
        List<CertificateEntity> certificates = certificateRepository.findAll();

    for (CertificateEntity certificate : certificates) {
        String json = publicApiClient.fetchScheduleList(year, certificate.getItemCode()); // xml → json
        if (json == null) continue; 

            try {
                JsonNode root = objectMapper.readTree(json);
                JsonNode items = root.path("body").path("items").path("item");

                if (items.isObject()) {
                    saveSchedule(items, certificate);
                } else if (items.isArray()) {
                    for (JsonNode item : items) {
                        saveSchedule(item, certificate);
                    }
                }
            } catch (Exception e) {
                log.error("시험일정 파싱 실패 ({}년 {}): {}", year, certificate.getItemCode(), e.getMessage());
            }
        }
        log.info("{}년 시험일정 동기화 완료", year);
    }

    private void saveSchedule(JsonNode item, CertificateEntity certificate) {
        String implYear = item.path("implYy").asText();
        Integer implSeq = item.path("implSeq").asInt();

        // 중복 저장 방지
        if (scheduleRepository.existsByImplYearAndImplSeqAndCertificate(implYear, implSeq, certificate)) {
            return;
        }
        ScheduleEntity entity = ScheduleEntity.builder()
                .certificate(certificate)
                .implYear(item.path("implYy").asText())
                .implSeq(item.path("implSeq").asInt())
                .description(item.path("description").asText())
                .writtenRegStart(parseDate(item.path("docRegStartDt").asText()))
                .writtenRegEnd(parseDate(item.path("docRegEndDt").asText()))
                .writtenExamStart(parseDate(item.path("docExamStartDt").asText()))
                .writtenExamEnd(parseDate(item.path("docExamEndDt").asText()))
                .writtenPassDate(parseDate(item.path("docPassDt").asText()))
                .practicalRegStart(parseDate(item.path("pracRegStartDt").asText()))
                .practicalRegEnd(parseDate(item.path("pracRegEndDt").asText()))
                .practicalExamStart(parseDate(item.path("pracExamStartDt").asText()))
                .practicalExamEnd(parseDate(item.path("pracExamEndDt").asText()))
                .practicalPassDate(parseDate(item.path("pracPassDt").asText()))
                .build();
        scheduleRepository.save(entity);
    }
}