package com.inspire.common.binding;

import com.inspire.common.binding.validation.constraints.NotAbsent;
import com.inspire.common.binding.validation.valueextraction.UpdateValueExtractor;
import com.inspire.common.binding.validation.constraintvalidators.NotAbsentValidator;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * A wrapper class representing a value that may be explicitly present or absent.
 *
 * <p>
 * This class is primarily intended for partial update semantics (e.g., HTTP PATCH).
 * Unlike {@link Optional}, it differentiates between an absent field and an explicit {@code null}.
 *
 * <p>
 * The value of type {@code Update} itself must not be {@code null} semantically.
 * To represent a traditional {@code null} value, use {@link #absent()}, analogous to {@link Optional#empty()}
 *
 * <p>
 * This type supports container element constraints defined by Jakarta Bean Validation
 * through the implementation of {@link UpdateValueExtractor}.
 *
 * <p>
 * Additionally, it provides the {@link NotAbsent @NotAbsent} constraint to enforce the presence of a value,
 * backed by {@link NotAbsentValidator}.
 *
 * <hr>
 * <p>
 * 명시적으로 존재하거나 누락된 값을 나타내는 래퍼 클래스.
 *
 * <p>
 * 이 클래스는 HTTP PATCH와 같은 부분적 업데이트 시나리오를 위해 설계되었습니다.
 * {@link Optional}과 달리, 누락된 필드와 명시적 {@code null} 값을 구분합니다.
 *
 * <p>
 * {@code Update} 타입의 값은 의미론적으로 {@code null}을 허용하지 않습니다.
 * 전통적인 의미의 {@code null}을 표현하려면 {@link Optional#empty()}와 유사하게 {@link #absent()}를 사용하십시오.
 *
 * <p>
 * {@link UpdateValueExtractor} 구현을 통해 Jakarta Bean Validation에서 정의하는 container element constraints를 지원합니다.
 *
 * <p>
 * 또한 {@link NotAbsentValidator}를 통해 값의 존재를 강제하기 위한 {@link NotAbsent @NotAbsent} 제약을 제공합니다.
 *
 * <hr>
 *
 * <p>
 * Typical usage:
 * <pre>{@code
 * Update<String> name = Update.present("John");
 * Update<String> nickname = Update.absent();
 * // Update<String> nickname = null; // semantically wrong usage
 *
 * name.ifPresent(System.out::println);
 * nickname.ifPresent(System.out::println);
 * }
 * </pre>
 *
 * <hr>
 *
 * <p>
 * With Bean Validation:
 * <pre>{@code
 * public class UserDTO {
 *     @NotAbsent // constraint on Update type itself
 *     private Update<@NotBlank String> name; // container element constraint
 *
 *     // JsonCreator will be applied in combination with Jackson,
 *     // since no NoArgsConstructor is defined.
 *     // it means that the default value of the field "name" will be Update.absent()
 *     public UserDTO(Update<String> name) {
 *         this.name = name;
 *     }
 * }
 * }</pre>
 *
 * <hr>
 *
 * @param <T> the wrapped value type
 * @author Wooseong Urm
 * @see NotAbsent
 * @see UpdateValueExtractor
 * @since 1.0.0
 */
public class Update<T> {

    /**
     * Wrapped value. <hr>
     * 래핑된 값.
     */
    private T value;

    /**
     * Flag indicating the presence of the value. <hr>
     * 값의 존재를 나타내는 플래그.
     */
    private boolean present;

    /**
     * Constructs a new {@code Update} instance with the provided value and presence.
     *
     * <hr>
     * <p>
     * 주어진 값과 존재 여부로 {@code Update} 인스턴스를 생성합니다.
     *
     * <hr>
     *
     * @param value   the wrapped value
     * @param present the presence of the value
     */
    public Update(T value, boolean present) {
        this.value = value;
        this.present = present;
    }

    /**
     * Returns the wrapped value. <hr>
     * <p>
     * 래핑된 값을 반환합니다. <hr>
     *
     * @return the wrapped value
     */
    public T getValue() {
        return this.value;
    }

    /**
     * Returns whether the value is present. <hr>
     * <p>
     * 값의 존재 여부를 반환합니다. <hr>
     *
     * @return {@code true} if the value is present; {@code false} otherwise
     */
    public boolean isPresent() {
        return this.present;
    }

    /**
     * Creates an {@code Update} instance representing a provided value.
     *
     * <hr>
     * <p>
     * 주어진 값으로 {@code Update} 인스턴스를 생성합니다.
     *
     * <hr>
     *
     * @param value the value
     * @param <T>   the wrapped value type
     * @return an {@code Update} marked as present
     */
    public static <T> Update<T> present(T value) {
        return new Update<>(value, true);
    }

    /**
     * Creates an {@code Update} instance representing an absent value.
     *
     * <hr>
     * <p>
     * 누락된 값을 나타내는 {@code Update} 인스턴스를 생성합니다.
     *
     * <hr>
     *
     * @param <T> the wrapped value type
     * @return an {@code Update} marked as absent
     */
    public static <T> Update<T> absent() {
        return new Update<>(null, false);
    }

    /**
     * Executes the provided {@link Consumer} if the value is marked as present.
     *
     * <hr>
     * <p>
     * 값이 존재하는 경우, 주어진 {@link Consumer}를 실행합니다.
     *
     * <hr>
     *
     * @param consumer the action to perform if the value is present
     */
    public void ifPresent(Consumer<T> consumer) {
        if (present) {
            consumer.accept(value);
        }
    }

    /**
     * Returns a string representation of this {@code Update} instance.
     *
     * <hr>
     * <p>
     * {@code Update} 인스턴스의 문자열 표현을 반환합니다.
     *
     * <hr>
     *
     * <p>
     * Formatted string:
     * <pre>
     * Update(value=&lt;value&gt;, present=&lt;true|false&gt;)
     * </pre>
     *
     * <hr>
     *
     * @return a string representation of this {@code Update} instance
     */
    @Override
    public String toString() {
        return "Update(value=" + String.valueOf(this.getValue()) + ", present=" + this.isPresent() + ")";
    }
}
