package com.inspire.certificate_service.service;

import com.inspire.certificate_service.domain.dto.*;
import com.inspire.certificate_service.domain.entity.CertificateEntity;
import com.inspire.certificate_service.domain.entity.ExamEntity;
import com.inspire.certificate_service.dao.CertificateRepository;
import com.inspire.certificate_service.dao.ExamRepository;
import com.inspire.certificate_service.dao.ExamSpecification;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExamService {

    private final ExamRepository examRepository;
    private final CertificateRepository certificateRepository;

    /**
     * [핵심] 엔티티를 평면형 DTO로 변환하는 매핑 메서드
     * 의도하신 대로 startDate, endDate, type(WR, PR 등)을 직접 매핑합니다.
     */
    private ExamListResponseDTO toListDto(ExamEntity entity) {
        return ExamListResponseDTO.builder()
                .itemCode(entity.getCertificate().getItemCode())
                .itemName(entity.getCertificate().getItemName())
                .largeFieldName(entity.getCertificate().getLargeFieldName())
                .type(entity.getType())         // WR, WE, PR, PE, PD 등
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .description(entity.getDescription())
                .build();
    }

    /**
     * 1. 요약 정보 조회
     */
    public ExamSummaryResponse getExamSummary() {
        LocalDate today = LocalDate.now();

        long activeCount = examRepository.countActiveWritten(today);
        long upcomingCount = examRepository.countUpcomingWritten(today);

        // 평면 구조이므로 3개의 접수 '행'을 그대로 변환하여 반환
        List<ExamListResponseDTO> activeExams = examRepository.findActiveWrittenExams(today, PageRequest.of(0, 3))
                .stream().map(this::toListDto).collect(Collectors.toList());

        List<ExamListResponseDTO> upcomingExams = examRepository.findUpcomingWrittenExams(today, PageRequest.of(0, 3))
                .stream().map(this::toListDto).collect(Collectors.toList());

        return ExamSummaryResponse.builder()
                .counts(ExamCountResponseDTO.builder()
                        .activeCount(activeCount)
                        .upcomingCount(upcomingCount)
                        .totalCount(activeCount + upcomingCount)
                        .build())
                .activeExams(activeExams)
                .upcomingExams(upcomingExams)
                .build();
    }

    /**
     * 2. 시험 목록 조회 (페이징)
     */
    public Page<ExamListResponseDTO> getExamList(String itemName, String fieldCode, String status, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        System.out.println("from: " + startDate);
        LocalDate today = LocalDate.now();

        // 필기 접수(WR)만 목록에 노출할지, 모든 일정을 노출할지에 따라 isWrittenRegistration() 사용 여부 결정
        Specification<ExamEntity> spec = Specification.where(ExamSpecification.withCertificate(itemName, fieldCode))
                .and(ExamSpecification.hasStatus(status, today))
                .and(ExamSpecification.isWithinRange(startDate, endDate));

        return examRepository.findAll(spec, pageable).map(this::toListDto);
    }

    /**
     * 3. 달력/범위 조회 (페이징 없음)
     * 평면 구조로 바뀌면서 기존의 복잡한 중복 제거(distinct) 로직이 필요 없어졌습니다.
     * 모든 타입(WR, WE, PR 등)의 개별 일정을 리스트로 쫙 뽑아줍니다.
     */
    public List<ExamListResponseDTO> getExamListByRange(String itemName, String fieldCode, String status, LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        Specification<ExamEntity> spec = Specification.where(ExamSpecification.isWithinRange(startDate, endDate))
                .and(ExamSpecification.hasStatus(status, today));

        // 해당 기간에 걸쳐있는 모든 일정 행을 DTO 리스트로 변환
        return examRepository.findAll(spec).stream()
                .map(this::toListDto)
                .collect(Collectors.toList());
    }

    /**
     * 4. 상세 조회 (기존의 그룹화 구조 유지)
     * 상세 페이지는 한 눈에 모든 정보를 봐야 하므로 ExamDetailResponseDTO 구조를 유지합니다.
     */
    public ExamDetailResponseDTO getCertificateWithSchedules(String itemCode) {
        CertificateEntity cert = certificateRepository.findById(itemCode)
                .orElseThrow(() -> new RuntimeException("자격증을 찾을 수 없습니다: " + itemCode));
        List<ExamEntity> allRows = examRepository.findByCertificate_ItemCode(itemCode);
        return ExamDetailResponseDTO.from(cert, allRows);
    }
}