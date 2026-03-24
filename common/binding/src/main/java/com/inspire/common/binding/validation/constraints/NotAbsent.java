package com.inspire.common.binding.validation.constraints;

import com.inspire.common.binding.Update;
import com.inspire.common.binding.validation.constraintvalidators.NotAbsentValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * A constraint ensuring that a wrapped value of {@link Update} is explicitly present.
 *
 * <p>
 * This constraint is intended to enforce the presence of a value,
 * regardless of the wrapped value itself.
 * The validation logic is implemented by {@link NotAbsentValidator}.
 *
 * <p>
 * This constraint must be applied to {@link Update} type directly,
 * not its type arguments of parameterized types.
 * </p>
 *
 * <hr>
 *
 * {@link Update} 내부에 래핑된 값이 명시적으로 존재함을 보장하는 제약 조건.
 *
 * <p>
 * 이 제약은 래핑된 값에 관계없이, 값의 존재를 강제하기 위한 제약 조건입니다.
 * 실제 검증 로직은 {@link NotAbsentValidator}에 의해 실행됩니다.
 *
 * <p>
 * 이 제약은 반드시 타입 매개변수가 아니라 {@link Update}에 직접 적용되어야 합니다.
 *
 * <hr>
 *
 *
 * @author Wooseong Urm
 * @since 1.0.1
 * @see Update
 * @see NotAbsentValidator
 *
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotAbsentValidator.class)
public @interface NotAbsent {

    /**
     * The validation error message. <hr>
     *
     * 검증 에러 메시지. <hr>
     *
     * @return the error message
     */
    String message() default "{binding.validation.constraints.NotAbsent.message}";

    /**
     * Validation groups to which this constraint belongs. <hr>
     *
     * 제약 조건이 속할 검증 그룹. <hr>
     *
     * @return an array of group classes
     */
    Class<?>[] groups() default {};

    /**
     * Metadata {@link Payload} to be consumed by a validation client. <hr>
     *
     * 검증 클라이언트에 의해 사용될 메타데이터 {@link Payload}. <hr>
     *
     * @return an array of {@code Payload} classes
     * @see Payload
     */
    Class<? extends Payload>[] payload() default {};
}
