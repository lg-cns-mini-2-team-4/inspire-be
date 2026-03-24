package com.inspire.certificate_service.domain.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExamSummaryResponse {

    private ExamCountResponseDTO counts;
    private List<ExamListResponseDTO> activeExams;   // 접수 중 3개
    private List<ExamListResponseDTO> upcomingExams; // 접수 예정 3개

}