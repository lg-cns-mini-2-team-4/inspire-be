package com.inspire.common.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 *
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {
    /**
     *
     */
    private String code;
    /**
     *
     */
    private String message;

    /**
     *
     */
    private List<ValidException> details;

    /**
     *
     * @param code
     * @param message
     */
    private ErrorResponse(String code, String message, List<ValidException> details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }

    public static ErrorResponse of(String code, String message, List<ValidException> details) {
        return new ErrorResponse(code, message, details);
    }

    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message, null);
    }

    public static ErrorResponse of(String code, List<ValidException> details) {
        return new ErrorResponse(code, null, details);
    }

    /**
     *
     * @return
     */
    public String getCode() {
        return this.code;
    }

    /**
     *
     * @return
     */
    public String getMessage() {
        return this.message;
    }

    public List<ValidException> getDetails() {
        return this.details;
    }
}
