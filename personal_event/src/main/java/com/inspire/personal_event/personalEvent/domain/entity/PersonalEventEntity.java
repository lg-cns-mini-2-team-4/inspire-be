package com.inspire.personal_event.personalEvent.domain.entity;

import java.time.LocalDate;

import com.inspire.personal_event.common.config.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="PERSONAL_EVENT_TBL")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalEventEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDate date;
    
    private EventType type;

    private String description;

    // user가 event 등록하는 것에 대한 관계
    @Column (nullable = false)
    private Long user;

    public void update(String title, LocalDate date, EventType type, String description) {
        this.title = title;
        this.date = date;
        this.type = type;
        this.description = description;
    }

}

