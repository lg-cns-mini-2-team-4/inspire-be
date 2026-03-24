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
    private Long id;
    private String title;
    private LocalDate date;
    private String type;
    private String description;
    private Long userId;
    private String refId;

    public static ScheduleResponseDTO fromEntity(ScheduleEntity entity) {
        return ScheduleResponseDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .date(entity.getDate())
                .type(entity.getType().name())
                .description(entity.getDescription())
                .userId(entity.getUserId())
                .refId(entity.getRefId())
                .build();
    }
}
