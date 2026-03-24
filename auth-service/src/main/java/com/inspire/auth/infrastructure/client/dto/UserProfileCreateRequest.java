package com.inspire.auth.infrastructure.client.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class UserProfileCreateRequest {

    private Long id;

    private String name;

    private String phone;

    private String email;

    @Builder
    public UserProfileCreateRequest(Long id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
}
