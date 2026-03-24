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
public class ScheduleRequestDTO {
    private Long userId;
    private String title;
    private LocalDate date;
    private String type;
    private String description;
    private Long refId;

    public ScheduleEntity toEntity(Long userId){
        return ScheduleEntity.builder()
                            .title(title)
                            .date(date)
                            .type(EventType.valueOf(type))
                            .description(description)
                            .userId(userId)
                            .build();
    }
}
