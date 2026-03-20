package com.inspire.user.domain.dto;

import com.inspire.user.domain.entity.UserEntity;
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

    public static UserResponseDTO fromEntity(UserEntity entity, String email) {
        return UserResponseDTO.builder()
                .name(entity.getName())
                .email(email)
                .build();
    }
}