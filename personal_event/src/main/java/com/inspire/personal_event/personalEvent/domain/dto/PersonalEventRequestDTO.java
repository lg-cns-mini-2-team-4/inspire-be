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
public class PersonalEventRequestDTO {

    private String title;
    private LocalDate date;
    private EventType type;
    private String description;

    public PersonalEventEntity toEntity(Long user){
        return PersonalEventEntity.builder()
                                    .title(this.title)
                                    .date(this.date)
                                    .type(this.type)
                                    .description(this.description)
                                    .user(user)
                                    .build();
    }
}
