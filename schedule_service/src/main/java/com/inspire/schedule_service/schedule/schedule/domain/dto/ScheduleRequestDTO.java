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
    private String title;
    private LocalDate date;
    private EventType type;
    private String description;

    public ScheduleEntity toEntity(Long user){
        return ScheduleEntity.builder()
                            .title(this.title)
                            .date(this.date)
                            .type(this.type)
                            .description(this.description)
                            .user(user)
                            .build();
    }
}
