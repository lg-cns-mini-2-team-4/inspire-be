package com.inspire.certificate_service.ctrl;


import com.inspire.certificate_service.domain.dto.ExamDetailResponseDTO;
import com.inspire.certificate_service.domain.dto.ExamListResponseDTO;
import com.inspire.certificate_service.domain.dto.ExamSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

public interface CertApiSpecification {

    @Operation(summary = "시험 요약")
    ResponseEntity<ExamSummaryResponse> getExamSummary();

    @Operation(summary = "모든 시험 조회")
    ResponseEntity<Page<ExamListResponseDTO>> getExams(
            @Parameter(name = "itemName", description = "item 이름", example = "정보처리기사")
            @RequestParam(name = "itemName", required = false) String itemName,
            @Parameter(name = "fieldCode", description = "종목 코드", example = "S")
            @RequestParam(name = "fieldCode", required = false) String fieldCode,
            @Parameter(name = "status", description = "접수 상태", example = "active")
            @RequestParam(name = "status", required = false) String status,
            @Parameter(name = "startDate", description = "조회 시작일", example = "2026-03-25")
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(name = "endDate", description = "조회 종료일", example = "2026-04-03")
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @ParameterObject Pageable pageable);

    @Operation(summary = "시험 상세 조회")
    ResponseEntity<ExamDetailResponseDTO> getCertificateWithSchedules(
            @Parameter(name = "itemCode", description = "시험 코드", example = "C04")
            @PathVariable String itemCode);
}
