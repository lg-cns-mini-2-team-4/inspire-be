package com.inspire.certificate_service.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExamCountResponseDTO {

    private long totalCount;     // 접수중 + 접수예정
    private long activeCount;    // 접수 중
    private long upcomingCount;  // 접수 예정

}
