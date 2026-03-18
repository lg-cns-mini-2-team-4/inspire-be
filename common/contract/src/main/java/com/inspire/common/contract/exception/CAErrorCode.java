package com.inspire.common.contract.exception;

/**
 *
 */
public enum CAErrorCode {
    INTERNAL_SERVER_ERROR("CA001", "서버 내부적으로 에러가 발생했습니다."),
    INVALID_REQUEST("CA002", "올바르지 않은 요청 형식입니다."),
    INTERNAL_SERVER_VALIDATION_ERROR("CA003", "서버 내부 검증 로직에 문제가 있습니다."),
    OTHER_MVC_EXCEPTION("CA004", "");

    private String code;
    private String message;

    CAErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
