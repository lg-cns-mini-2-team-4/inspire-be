package com.inspire.user.exception;

import com.inspire.common.core.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(404, "USR001", "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(409, "USR002", "User ID 중복입니다."),
    MISSING_AUTH_TOKEN(401, "ATH004", "인증 정보가 부족합니다."),
    TOKEN_CONFLICT(401, "ATH005", "토큰 정보가 일치하지 않습니다."),
    REFRESH_NOT_IN_REDIS(401, "ATH006", "유효하지 않은 토큰입니다.");

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
