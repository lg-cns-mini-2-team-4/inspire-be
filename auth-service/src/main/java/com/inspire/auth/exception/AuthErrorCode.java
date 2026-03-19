package com.inspire.auth.exception;

import com.inspire.common.core.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AuthErrorCode implements ErrorCode {
    TEST(401, "ATH001", "에러"),
    USER_NOT_FOUND(404, "ATH002", "사용자를 찾을 수 없습니다."),
    INVALID_PASSWORD(401, "ATH003", "비밀번호가 일치하지 않습니다."),
    USER_ALREADY_EXISTS(409, "ATH004", "이미 가입된 이메일입니다.");

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
