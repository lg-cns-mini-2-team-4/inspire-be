package com.inspire.user.mapper;

import com.inspire.user.domain.dto.request.UserCreateRequest;
import com.inspire.user.domain.dto.response.UserResponse;
import com.inspire.user.infrastructure.entity.UserEntity;

public class UserEntityMapper {
    public static UserEntity fromUserCreate(UserCreateRequest request) {
        return UserEntity.builder()
                .id(request.getId())
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .build();
    }

    public static UserResponse toUserResponse(UserEntity userEntity) {
        return UserResponse.builder()
                .userId(userEntity.getId())
                .name(userEntity.getName())
                .phone(userEntity.getPhone())
                .email(userEntity.getEmail())
                .build();
    }
}
