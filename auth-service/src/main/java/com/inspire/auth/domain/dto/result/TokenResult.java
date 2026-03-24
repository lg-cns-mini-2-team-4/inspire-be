package com.inspire.auth.domain.dto.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenResult {
    String accessToken;
    String refreshToken;
}
