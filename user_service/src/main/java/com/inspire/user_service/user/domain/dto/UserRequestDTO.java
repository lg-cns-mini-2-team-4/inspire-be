package com.inspire.user_service.user.domain.dto;

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
public class UserRequestDTO {
    
    // Auth 연동 전이므로 프로필 수정은 일단 이름(name)만
    private String name;
    private String email;
    private String phone;

    public UserEntity toEntity(Long userId) {
        return UserEntity.builder()
                .userId(userId)
                .name(this.name)
                .email(this.email)
                .phone(this.phone)
                .build();
    }

}