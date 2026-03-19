package com.inspire.auth.exception;

import com.inspire.common.core.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AuthErrorCode implements ErrorCode {
    REFRESH_NOT_IN_REDIS(401, "ATH001", "유효하지 않은 토큰입니다."),
    REFRESH_CONFLICT_WITH_REDIS(401, "ATH002", "유효하지 않은 토큰입니다."),
    TEST(401, "ATH001", "에러")
    ;

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
