package com.inspire.user.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class UserCreateRequest {

    @NotNull
    private Long id;

    @NotBlank
    private String name;
    private String phone;
    @NotBlank
    private String email;
}
