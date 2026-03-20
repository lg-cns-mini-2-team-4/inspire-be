package com.inspire.auth.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "토큰 응답 데이터 (Access Token 및 만료 시간)")
public class TokenResponse {

    @Schema(description = "Access Token (Bearer)",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoYWtiaW4xMjMifQ...")
    private String accessToken;

    @Schema(description = "토큰 만료 시간 ()")
    private Integer expires;
}
