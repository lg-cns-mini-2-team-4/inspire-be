package com.inspire.user_service.user.domain.dto;

import java.time.LocalDate;

import com.inspire.user_service.user.domain.entity.UserEntity;

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
public class UserResponseDTO {

    private String name;
    private String email;
    private String phone;

    public static UserResponseDTO fromEntity(UserEntity entity) {
        return UserResponseDTO.builder()
                                .name(entity.getName())
                                .phone(entity.getPhone())
                                .email(entity.getEmail())
                                .build();
    }
}