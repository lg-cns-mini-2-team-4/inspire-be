// package com.example.certificate_service.service;

// import com.example.certificate_service.domain.dto.*;
// import com.example.certificate_service.domain.entity.CertificateEntity;
// import com.example.certificate_service.domain.entity.ExamEntity;
// import com.example.certificate_service.dao.CertificateRepository;
// import com.example.certificate_service.dao.ExamRepository;
// import com.example.certificate_service.dao.ExamSpecification;

// import lombok.RequiredArgsConstructor;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.jpa.domain.Specification;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.time.LocalDate;
// import java.util.List;
// import java.util.stream.Collectors;

// @Service
// @RequiredArgsConstructor
// @Transactional(readOnly = true)
// public class ExamService {

//     private final ExamRepository examRepository;
//     private final CertificateRepository certificateRepository; // 추가 필요

//     public ExamSummaryResponse getExamSummary() {
//         LocalDate today = LocalDate.now(); // 2026-03-24

//         // 1. 카운트 계산
//         long activeCount = examRepository.countActiveWritten(today);
//         long upcomingCount = examRepository.countUpcomingWritten(today);

//         ExamCountResponseDTO counts = ExamCountResponseDTO.builder()
//                 .activeCount(activeCount)
//                 .upcomingCount(upcomingCount)
//                 .totalCount(activeCount + upcomingCount)
//                 .build();

//         // 2. 접수 중(필기 WR) 상위 3개
//         List<ExamListResponseDTO> activeExams = examRepository.findActiveWrittenExams(today, PageRequest.of(0, 3))
//                 .stream().map(this::mapToFullScheduleDto).collect(Collectors.toList());

//         // 3. 접수 예정(필기 WR) 상위 3개
//         List<ExamListResponseDTO> upcomingExams = examRepository.findUpcomingWrittenExams(today, PageRequest.of(0, 3))
//                 .stream().map(this::mapToFullScheduleDto).collect(Collectors.toList());

//         return ExamSummaryResponse.builder()
//                 .counts(counts)
//                 .activeExams(activeExams)
//                 .upcomingExams(upcomingExams)
//                 .build();
//     }

//     /**
//      * 특정 시험 행(WR 또는 PR)을 기준으로 해당 회차의 전체 일정(필기/실기 전체)을 찾아 DTO로 병합
//      */
//     private ExamListResponseDTO mapToFullScheduleDto(ExamEntity baseExam) {
//         List<ExamEntity> fullSchedule = examRepository.findByCertificate_ItemCodeAndImplYearAndImplSeq(
//                 baseExam.getCertificate().getItemCode(),
//                 baseExam.getImplYear(),
//                 baseExam.getImplSeq()
//         );

//         ExamListResponseDTO.ExamListResponseDTOBuilder builder = ExamListResponseDTO.builder()
//                 .itemCode(baseExam.getCertificate().getItemCode())
//                 .itemName(baseExam.getCertificate().getItemName())
//                 .largeFieldName(baseExam.getCertificate().getLargeFieldName())
//                 .description(baseExam.getDescription());

//         for (ExamEntity e : fullSchedule) {
//             switch (e.getType()) {
//                 case "WR": builder.writtenRegStart(e.getStartDate()).writtenRegEnd(e.getEndDate()); break;
//                 case "WE": builder.writtenExamStart(e.getStartDate()).writtenExamEnd(e.getEndDate()); break;
//                 case "WP": builder.writtenPassDate(e.getEndDate()); break;
//                 case "PR": builder.practicalRegStart(e.getStartDate()).practicalRegEnd(e.getEndDate()); break;
//                 case "PE": builder.practicalExamStart(e.getStartDate()).practicalExamEnd(e.getEndDate()); break;
//                 case "PD": builder.practicalPassDate(e.getEndDate()); break;
//             }
//         }
//         return builder.build();
//     }

//     // ExamService.java 에 추가

//     public Page<ExamListResponseDTO> getExamList(String itemName, String fieldCode, String status, Pageable pageable) {
//         LocalDate today = LocalDate.now();

//         // 1. Specification 조합
//         Specification<ExamEntity> spec = Specification.where(ExamSpecification.isWrittenRegistration())
//                 .and(ExamSpecification.hasItemName(itemName))
//                 .and(ExamSpecification.hasLargeField(fieldCode))
//                 .and(ExamSpecification.hasStatus(status, today));

//         // 2. 페이징 조회
//         Page<ExamEntity> examPage = examRepository.findAll(spec, pageable);

//        // ExamService.java 에 추가 (기존 코드 유지하며 추가)

//         // 3. DTO 변환 (각 항목마다 상세 일정 조립)
//         return examPage.map(this::mapToFullScheduleDto);
//     }

//     // 3. [누락 보완] 달력/범위 조회 (페이징 없음)
//     public List<ExamListResponseDTO> getExamListByRange(String itemName, String fieldCode, String status, LocalDate startDate, LocalDate endDate) {
//         LocalDate today = LocalDate.now();
//         Specification<ExamEntity> spec = Specification.where(ExamSpecification.isWithinRange(startDate, endDate))
//                 .and(ExamSpecification.hasItemName(itemName))
//                 .and(ExamSpecification.hasLargeField(fieldCode))
//                 .and(ExamSpecification.hasStatus(status, today));

//         List<ExamEntity> allMatches = examRepository.findAll(spec);

//         // 중복 회차 제거 후 DTO 변환
//         return allMatches.stream()
//                 .map(e -> e.getCertificate().getItemCode() + "-" + e.getImplYear() + "-" + e.getImplSeq())
//                 .distinct()
//                 .map(key -> {
//                     String[] parts = key.split("-");
//                     ExamEntity base = allMatches.stream()
//                             .filter(e -> e.getCertificate().getItemCode().equals(parts[0])
//                                       && e.getImplYear().equals(parts[1])
//                                       && e.getImplSeq().toString().equals(parts[2]))
//                             .findFirst().get();
//                     return mapToFullScheduleDto(base);
//                 })
//                 .collect(Collectors.toList());
//     }

//     // 4. [누락 보완] 상세 조회 (MainController에서 호출하는 이름으로 통일)
//     public ExamDetailResponseDTO getCertificateWithSchedules(String itemCode) {
//         CertificateEntity cert = certificateRepository.findById(itemCode)
//                 .orElseThrow(() -> new RuntimeException("자격증을 찾을 수 없습니다: " + itemCode));
//         List<ExamEntity> allRows = examRepository.findByCertificate_ItemCode(itemCode);
//         return ExamDetailResponseDTO.from(cert, allRows);
//     }

// }

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