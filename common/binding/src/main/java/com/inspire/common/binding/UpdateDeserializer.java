package com.inspire.common.binding;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * A custom Jackson {@link StdDeserializer} for deserializing {@link Update Update&lt;T&gt;}.
 *
 * <p>
 * This deserializer enables partial update semantics.
 *
 * <p>
 * The actual inner type {@code T} is resolved contextually using {@link #createContextual(DeserializationContext, BeanProperty)}.
 *
 * <hr>
 * <p>
 * {@link Update Update&lt;T&gt;}를 역직렬화 하기 위한 커스텀 {@link StdDeserializer}.
 *
 * <p>
 * 부분적인 업데이트 구문을 지원합니다.
 *
 * <p>
 * {@link #createContextual(DeserializationContext, BeanProperty)}을 사용하여 실제 내부 타입인 {@code T}를 해석합니다.
 *
 * <hr>
 *
 * <p>
 * Semantics:
 * <ul>
 *     <li><b>Absent value</b> – The JSON field is missing.</li>
 *     <li><b>Present null</b> – The JSON field is explicitly set to {@code null}.</li>
 *     <li><b>Present value</b> – The JSON field contains a non-null value.</li>
 * </ul>
 *
 * <hr>
 *
 * @param <T> the wrapped value type
 * @author Wooseong Urm
 * @since 1.0.0
 */
@Slf4j
public class UpdateDeserializer<T> extends StdDeserializer<Update<T>>
        implements ContextualDeserializer {

    /**
     * Inner JavaType value. <hr>
     * 내부 JavaType 값. <hr>
     *
     * @see JavaType
     */
    private final JavaType innerType;

    /**
     * Constructs a new {@code UpdateDeserializer} instance with no arguments.
     *
     * <p>
     * Used only for module registration. the actual inner type will be resolved later via
     * {@link #createContextual(DeserializationContext, BeanProperty)}.
     *
     * <hr>
     * <p>
     * 인자 없이 {@code UpdateDeserializer}를 생성합니다.
     *
     * <p>
     * 모듈 등록을 위해서만 사용됩니다. 실제 내부 타입은
     * {@link #createContextual(DeserializationContext, BeanProperty)}에 의해 해석됩니다.
     *
     * <hr>
     *
     * @see #createContextual(DeserializationContext, BeanProperty)
     */
    public UpdateDeserializer() {
        super(Update.class);
        log.debug("UpdateDeserializer initialized");
        this.innerType = null;
    }

    /**
     * Creates a contextual copy used internally to deserialize {@link Update Update&lt;T&gt;} with a resolved inner type.
     *
     * <hr>
     * <p>
     * 해석된 내부 타입을 기반으로 {@link Update Update&lt;T&gt;}를 역직렬화 할 수 있는 contextual copy를 생성합니다.
     *
     * <hr>
     *
     * @param src       the source deserializer
     * @param innerType the resolved inner value type
     * @see #createContextual(DeserializationContext, BeanProperty)
     */
    private UpdateDeserializer(UpdateDeserializer<?> src, JavaType innerType) {
        super(src);
        log.debug("UpdateDeserializer initialized with (innerType: {})", innerType);
        this.innerType = innerType;
    }

    /**
     * Resolves the actual generic type from {@link Update Update&lt;T&gt;}
     * and creates a contextual copy with that type.
     *
     * <hr>
     * <p>
     * {@link Update Update&lt;T&gt;}로부터 제네릭 타입인 {@code T}를 해석하고,
     * 그 타입을 바탕으로 contextual copy를 생성합니다.
     *
     * <hr>
     *
     * @param ctxt     the deserialization context
     * @param property the bean property being deserialized
     * @return a contextual copy of {@code UpdateDeserializer} with the resolved inner type
     */
    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        if (property == null) {
            log.warn("Contextual copy returned the same deserializer since property is null");
            return this;
        }

        log.debug("property (getName: {}, getType: {})", property.getName(), property.getType());

        JavaType wrapperType = property.getType(); // Update<T>
        JavaType innerType = wrapperType.containedType(0); // T
        JavaType actualType = innerType != null
                ? innerType
                : ctxt.constructType(Object.class);

        log.debug("Contextual copy created with (wrapper: {}, actualType: {})",
                wrapperType, actualType);

        return new UpdateDeserializer<>(this, actualType);
    }

    /**
     * Deserializes JSON into an {@link Update} wrapper.
     *
     * <p>
     * Distinguishes between absent fields, explicit {@code null},
     * and non-null values.
     *
     * <hr>
     * <p>
     * JSON을 {@link Update} 래퍼 클래스로 역직렬화 합니다.
     *
     * <p>
     * 누락된 필드, 명시적 {@code null}, null이 아닌 값을 구분합니다.
     *
     * <hr>
     *
     * @param p    the JSON parser
     * @param ctxt the deserialization context
     * @return an {@code Update<T>} instance representing the parsed state
     * @throws InvalidUpdateUsageException if invoked as a root type instead of a DTO property
     */
    @Override
    @SuppressWarnings("unchecked")
    public Update<T> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        // empty body
        if (p.currentToken() == null) {
            return Update.absent();
        }

        // root usage check
        if (innerType == null) {
            throw new InvalidUpdateUsageException(
                    "Update must be used inside a DTO, not as a root type"
            );
        }

        JsonDeserializer<Object> delegate = ctxt.findContextualValueDeserializer(innerType, null);

        Object value = delegate.deserialize(p, ctxt);

        return Update.present((T) value);
    }

    /**
     * Handles explicit null values.
     *
     * <hr>
     * <p>
     * 명시적인 null 값을 처리합니다.
     *
     * <hr>
     *
     * @param ctxt the deserialization context
     * @return an {@link Update} wrapper with a present null
     */
    @Override
    public Update<T> getNullValue(DeserializationContext ctxt) {
        return Update.present(null);
    }

    /**
     * Handles absent values.
     *
     * <p>
     * This method is intended for use by Jackson's creator binding
     * (constructor or factory methods) when an {@link Update} property
     * is missing in the input JSON.
     *
     * <hr>
     * <p>
     * 존재하지 않는 필드를 나타냅니다.
     *
     * <p>
     * Jackson의 creator binding (생성자 또는 팩토리 메소드) 전용 메소드입니다.
     * JSON 입력에 {@link Update} 프로퍼티가 누락되었을 때 사용됩니다.
     *
     * @param ctxt the deserialization context
     * @return an {@link Update} wrapper with an absent null.
     */
    @Override
    public Object getAbsentValue(DeserializationContext ctxt) {
        return Update.absent();
    }
}