package com.inspire.common.binding.validation.valueextraction;

import com.inspire.common.binding.Update;
import jakarta.validation.valueextraction.ExtractedValue;
import jakarta.validation.valueextraction.ValueExtractor;
import lombok.extern.slf4j.Slf4j;

/**
 * A {@link ValueExtractor} implementation for {@link Update Update&lt;T&gt;}.
 *
 * <p>
 * This extractor allows Jakarta Bean Validation to apply
 * container element constraints to the {@link Update Update&lt;T&gt;} type.
 * It extracts the underlying value of type {@code T} and passes it to the validation engine.
 *
 * <hr>
 *
 * {@link Update Update&lt;T&gt;}에 대한 {@link ValueExtractor} 구현체.
 *
 * <p>
 * Jakarta Bean Validation이 {@link Update Update&lt;T&gt;} 타입에 container element constraints를 적용할 수 있도록 합니다.
 * 내부에 래핑된 {@code T} 타입의 값을 추출하여 검증 엔진에 전달합니다.
 *
 *
 * <hr>
 *
 * @author Wooseong Urm
 * @since 1.0.1
 * @see Update
 * @see ValueExtractor
 */
@Slf4j
public class UpdateValueExtractor implements ValueExtractor<Update<@ExtractedValue ?>> {

    /**
     * Extracts the wrapped value of type {@code T} from the provided {@link Update Update&lt;T&gt;} instance and
     * passes it to the provided {@link ValueExtractor.ValueReceiver}.
     *
     * <p>
     * The extracted value is associated with the node name {@code value}.
     *
     * <hr>
     *
     * 주어진 {@link Update Update&lt;T&gt;} 인스턴스 내부에 래핑된 {@code T} 타입의 값을 추출하고
     * {@link ValueReceiver}에게 전달합니다.
     *
     * <p>
     * 추출된 값은 {@code value}라는 노드 이름으로 연결됩니다.
     *
     * <hr>
     *
     * @param originalValue the {@link Update Update&lt;T&gt;} instance containing the value of type {@code T} to extract
     * @param receiver      the {@link ValueExtractor.ValueReceiver} that will receive the extracted value
     */
    @Override
    public void extractValues(Update<?> originalValue, ValueReceiver receiver) {
        Object wrappedValue = originalValue.getValue();
        log.debug("The extracted wrapped value of Update: {}", wrappedValue);
        receiver.value("value", wrappedValue);
    }
}
