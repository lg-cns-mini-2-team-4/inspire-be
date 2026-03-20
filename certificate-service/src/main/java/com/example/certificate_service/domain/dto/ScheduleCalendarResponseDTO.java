package com.example.certificate_service.domain.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@Builder
public class ScheduleCalendarResponseDTO {
    private String itemCode;             // 종목 코드
    private String itemName;             // 종목 이름
    private String largeFieldName;       // 대직무분야 명
    
    private LocalDate writtenRegStart;   // 필기시험 원서접수 시작
    private LocalDate writtenRegEnd;     // 필기시험 원서접수 종료
    private LocalDate writtenExamStart;  // 필기시험 시작
    private LocalDate writtenExamEnd;    // 필기시험 종료
    private LocalDate writtenPassDate;   // 필기시험 합격 발표일
    
    private LocalDate practicalExamStart;// 실기 시작
    private LocalDate practicalExamEnd;  // 실기 종료
}