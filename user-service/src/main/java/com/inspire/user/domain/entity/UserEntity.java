package com.inspire.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "User")
@Getter
public class UserEntity {
    @Id
    @Column(name = "user_id")
    private Long userId;

    private String name;

    public void update(String name) {
        this.name = name;
    }
}