package com.inspire.schedule_service.schedule.schedule.domain.dto;

import java.time.LocalDate;

import com.inspire.schedule_service.schedule.schedule.domain.entity.EventType;
import com.inspire.schedule_service.schedule.schedule.domain.entity.ScheduleEntity;

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
public class ScheduleResponseDTO {
    private String title;
    private LocalDate date;
    private EventType type;
    private String description;

    // 시험 등록 필드
    private String itemName;
    private LocalDate writtenRegStart;
    private LocalDate writtenRegEnd;
    private LocalDate writtenExamStart;
    private LocalDate writtenPassDate;
    private LocalDate practicalRegStart;
    private LocalDate practicalRegEnd;
    private LocalDate practicalExamStart;
    private LocalDate practicalPassDate;

    public static ScheduleResponseDTO fromEntity(ScheduleEntity entity){
        return ScheduleResponseDTO.builder()
                                    .title(entity.getTitle())
                                    .date(entity.getDate())
                                    .type(entity.getType())
                                    .description(entity.getDescription())
                                    .build();
    }
}
