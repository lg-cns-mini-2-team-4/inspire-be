package com.inspire.common.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inspire.common.core.exception.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 *
 * @param <T>
 */
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    /**
     *
     */
    private boolean success;
    /**
     *
     */
    private int status;
    /**
     *
     */
    private T data;
    /**
     *
     */
    private ErrorResponse error;
    /**
     *
     */
    private LocalDateTime timestamp;

    /**
     *
     * @param success
     * @param status
     * @param data
     * @param error
     * @param timestamp
     */
    private ApiResponse(boolean success, int status, T data, ErrorResponse error, LocalDateTime timestamp) {
        this.success = success;
        this.status = status;
        this.data = data;
        this.error = error;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    }

    /**
     *
     * @param status
     * @param data
     * @param timestamp
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> ofSuccess(int status, T data, LocalDateTime timestamp) {
        return new ApiResponse<>(true, status, data, null, timestamp);
    }

    /**
     *
     * @param status
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> ofSuccess(int status, T data) {
        return new ApiResponse<>(true, status, data, null, null);
    }

    /**
     *
     * @param data
     * @param timestamp
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> ofSuccess(T data, LocalDateTime timestamp) {
        return new ApiResponse<>(true, 200, data, null, timestamp);
    }

    /**
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> ofSuccess(T data) {
        return new ApiResponse<>(true, 200, data, null, null);
    }

    /**
     *
     * @param status
     * @param error
     * @param timestamp
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> ofError(int status, ErrorResponse error, LocalDateTime timestamp) {
        return new ApiResponse<>(false, status, null, error, timestamp);
    }

    /**
     *
     * @param status
     * @param error
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> ofError(int status, ErrorResponse error) {
        return new ApiResponse<>(false, status, null, error, null);
    }

    /**
     *
     * @param status
     * @param code
     * @param message
     * @param timestamp
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> ofError(int status, String code, String message, LocalDateTime timestamp) {
        return new ApiResponse<>(false, status, null, ErrorResponse.of(code, message), timestamp);
    }

    /**
     *
     * @param status
     * @param code
     * @param message
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> ofError(int status, String code, String message) {
        return new ApiResponse<>(false, status, null, ErrorResponse.of(code, message), null);
    }

    public static <T> ApiResponse<T> ofError(int status, String code, String message, List<ValidException> details, LocalDateTime timestamp) {
        return new ApiResponse<>(false, status, null, ErrorResponse.of(code, message, details), timestamp);
    }

    public static <T> ApiResponse<T> ofError(int status, String code, String message, List<ValidException> details) {
        return new ApiResponse<>(false, status, null, ErrorResponse.of(code, message, details), null);
    }

    public static <T> ApiResponse<T> ofError(int status, String code, List<ValidException> details, LocalDateTime timestamp) {
        return new ApiResponse<>(false, status, null, ErrorResponse.of(code, details), timestamp);
    }

    public static <T> ApiResponse<T> ofError(int status, String code, List<ValidException> details) {
        return new ApiResponse<>(false, status, null, ErrorResponse.of(code, details), null);
    }

    public static <T> ApiResponse<T> ofError(ErrorCode errorCode, LocalDateTime timestamp) {
        int status = errorCode.getStatus();
        String code = errorCode.getCode();
        String message = errorCode.getMessage();
        return new ApiResponse<>(false, status, null, ErrorResponse.of(code, message), timestamp);
    }

    public static <T> ApiResponse<T> ofError(ErrorCode errorCode) {
        int status = errorCode.getStatus();
        String code = errorCode.getCode();
        ;
        String message = errorCode.getMessage();
        return new ApiResponse<>(false, status, null, ErrorResponse.of(code, message), null);
    }
}
