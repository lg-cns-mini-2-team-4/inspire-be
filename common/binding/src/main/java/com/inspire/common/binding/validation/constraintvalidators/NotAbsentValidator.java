package com.inspire.common.binding.validation.constraintvalidators;

import com.inspire.common.binding.Update;
import com.inspire.common.binding.validation.constraints.NotAbsent;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

/**
 * A {@link ConstraintValidator} implementation for the {@link NotAbsent @NotAbsent} constraint.
 *
 * <p>
 * This validator checks whether the {@link Update Update} represents a present value,
 * regardless of the wrapped value itself.
 *
 * <hr>
 *
 * {@link NotAbsent @NotAbsent} 제약 조건을 위한 검증 구현체.
 *
 * <p>
 * {@link Update} 내부에 래핑된 값에 상관 없이, 값이 존재하는지만 확인합니다.
 *
 * <hr>
 *
 * @author Wooseong Urm
 * @since 1.0.1
 * @see NotAbsent
 * @see ConstraintValidator
 */
@Slf4j
@SuppressWarnings("rawtypes")
public class NotAbsentValidator implements ConstraintValidator<NotAbsent, Update> {

    /**
     * Constructs a new {@code NotAbsentValidator} instance.
     *
     * <hr>
     *
     * {@code NotAbsentValidator} 인스턴스를 생성합니다.
     */
    public NotAbsentValidator() {
        log.debug("NotAbsentValidator initialized");
    }

    /**
     * Validates that the provided {@link Update} value is present. <hr>
     *
     * 주어진 {@link Update}의 값이 존재하는지 검증합니다. <hr>
     *
     * @param value     {@code Update} to validate
     * @param context   context in which the constraint is evaluated
     *
     * @return {@code true} if the value is present; {@code false} otherwise
     */
    @Override
    public boolean isValid(Update value, ConstraintValidatorContext context) {
        boolean valid = value.isPresent();
        if(!valid) {
            log.debug("The present of Update must not be true");
        }
        return value.isPresent();
    }
}
