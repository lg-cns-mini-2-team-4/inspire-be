package com.inspire.auth.exception;

import com.inspire.common.core.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AuthErrorCode implements ErrorCode {
    REFRESH_NOT_IN_REDIS(401, "ATH001", "유효하지 않은 토큰입니다."),
    MISSING_AUTH_TOKEN(401, "ATH002", "인증 정보가 부족합니다."),
    TOKEN_CONFLICT(401, "ATH003", "토큰 정보가 일치하지 않습니다."),
    INVALID_CREDENTIALS(401, "ATH004", "아이디 또는 비밀번호가 일치하지 않습니다.");

    private final int status;
    private final String code;
    private final String message;

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
