package com.inspire.auth.domain.dto.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReissueTokenResult {
    String accessToken;
    String refreshToken;
    Integer accessExpires;
    Integer refreshExpires;
}
