package com.inspire.common.binding;

/**
 * {@code RuntimeException} thrown when an {@link Update} is used in invalid ways.
 *
 * <hr>
 *
 * {@link Update}가 잘못된 방법으로 사용될 때 발생하는 {@code RuntimeException}
 *
 * <hr>
 *
 * @author Wooseong Urm
 * @since 1.0.0
 */
public class InvalidUpdateUsageException extends RuntimeException {

    /**
     * Constructs a new {@code InvalidUpdateUsageException} instance with {@code null} as its detail message.
     *
     * <hr>
     *
     * 상세 메시지가 없는 {@code InvalidUpdateUsageException}을 생성합니다.
     */
    public InvalidUpdateUsageException() {}

    /**
     * Constructs a new {@code InvalidUpdateUsageException} with the specified detail message.
     *
     * <hr>
     *
     * 상세 메시지를 포함하여 {@code InvalidUpdateUsageException}을 생성합니다.
     *
     * <hr>
     *
     * @param message the detail message describing the error
     *                (에러를 설명하는 상세 메시지)
     */
    public InvalidUpdateUsageException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code InvalidUpdateUsageException} with the specified detail message and cause.
     *
     * <hr>
     *
     * 상세 메시지 및 원인을 포함하여 {@code InvalidUpdateUsageException}을 생성합니다.
     *
     * <hr>
     *
     * @param message the detail message describing the error
     *                (에러를 설명하는 상세 메시지)
     * @param cause the underlying cause of the exception
     *              (에러가 발생한 근본 원인)
     */
    public InvalidUpdateUsageException(String message, Throwable cause) {
        super(message, cause);
    }
}
