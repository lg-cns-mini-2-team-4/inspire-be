package com.inspire.personal_event.personalEvent.domain.dto;

import java.time.LocalDate;

import com.inspire.personal_event.personalEvent.domain.entity.EventType;
import com.inspire.personal_event.personalEvent.domain.entity.PersonalEventEntity;

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
public class PersonalEventResponseDTO {

    private String title;
    private LocalDate date;
    private EventType type;
    private String description;

    public static PersonalEventResponseDTO fromEntity(PersonalEventEntity entity){
        return PersonalEventResponseDTO.builder()
                                        .title(entity.getTitle())
                                        .date(entity.getDate())
                                        .type(entity.getType())
                                        .description(entity.getDescription())
                                        .build();
    }
}
