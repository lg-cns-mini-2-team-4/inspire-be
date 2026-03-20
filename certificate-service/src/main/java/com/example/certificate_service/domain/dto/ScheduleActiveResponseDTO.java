package com.example.certificate_service.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ScheduleActiveResponseDTO {
    private String itemCode;           // 종목 코드
    private String itemName;           // 종목 이름
    private String largeFieldName;     // 대직무분야 명
    private String description;        // 설명
    private String officeName;         // 지사명
    private String examLocation;       // 시행장소

    private LocalDate writtenRegStart; // 필기시험 원서접수 시작
    private LocalDate writtenRegEnd;   // 필기시험 원서접수 종료
    private LocalDate writtenExamStart;// 필기시험 시작
    private LocalDate writtenExamEnd;  // 필기시험 종료
    
    private LocalDate practicalRegStart;     // 실기시험 원서 접수 시작
    private LocalDate practicalRegEnd;       // 실기시험 원서 접수 종료
    private LocalDate practicalExamStart;    // 실기시험 시작
    private LocalDate practicalExamEnd;      // 실기시험 종료
}
