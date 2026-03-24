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
// schedule DB에 저장을 위한 DTO
public class ScheduleRequestDTO {
    private String title;
    private String description;
    private LocalDate date;
    private EventType type;
    private Long userId; 
    private String examId;

    public ScheduleEntity toEntity(Long userId) {
        return ScheduleEntity.builder()
                            .title(this.title)
                            .description(this.description)
                            .date(this.date)
                            .type(this.type)
                            .userId(userId)
                            .refId(this.examId)
                            .build();
    }
}
