package com.inspire.schedule_service.schedule.schedule.domain.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExamScheduleRequestDTO {
    // 종목 식별을 위한 기본 정보
    private String itemCode;           
    private String itemName;           

    // 필기시험 관련 날짜
    private LocalDate writtenRegStart; 
    private LocalDate writtenRegEnd;   
    private LocalDate writtenExamStart;
    private LocalDate writtenExamEnd;  
    private LocalDate writtenPassDate; 

    // 실기시험 관련 날짜
    private LocalDate practicalRegStart;    
    private LocalDate practicalRegEnd;      
    private LocalDate practicalExamStart;   
    private LocalDate practicalExamEnd;     
    private LocalDate practicalPassDate;    

    // 추가 정보
    private String description;
    private String officeName;
    private String examLocation;
}
