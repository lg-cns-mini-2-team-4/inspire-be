package com.inspire.common.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.inspire.common.binding.InvalidUpdateUsageException;
import lombok.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Tests for UpdateDeserializer")
public class UpdateDeserializerTest {

    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    static class UpdateTestDTO {
        private Update t1 = Update.absent();
        private Update<String> t2 = Update.absent();
        private Update<String> t3 = Update.absent();
    }

    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    static class GenericTestDTO {
        private Update<List<String>> t1 = Update.absent();
        private Update<String> t2 = Update.absent();
    }

    @Getter
    @Setter
    @ToString
    static class ConstructorTestDTO {
        private Update<String> t1;

        public ConstructorTestDTO() {

        }

        public ConstructorTestDTO(@JsonProperty("t1") Update<String> t1) {
            this.t1 = t1;
        }
    }

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        SimpleModule updateModule = new SimpleModule();
        updateModule.addDeserializer(Update.class, new UpdateDeserializer<>());

        objectMapper = Jackson2ObjectMapperBuilder.json()
                .modules(updateModule)
                .build();
    }

    @Test
    @DisplayName("deserialize a JSON payload with JsonCreator")
    void deserializeJsonWithJsonCreator() throws JsonProcessingException {
        String json = "{}";

        ConstructorTestDTO dto = objectMapper.readValue(json, ConstructorTestDTO.class);
        System.out.println(dto);

        assertUpdate(dto.getT1(), false, null);
    }

    @Test
    @DisplayName("deserialize a JSON payload with java.util.List")
    void deserializeJsonWithListToUpdate() throws JsonProcessingException {
        String json = "{\"t1\":[\"test\", \"value\", \"list\"]}";

        GenericTestDTO dto = objectMapper.readValue(json, GenericTestDTO.class);

        System.out.println(dto);

        assertUpdate(dto.getT1(), true, List.of("test", "value", "list"));
        assertUpdate(dto.getT2(), false, null);
    }

    @Test
    @DisplayName("should throw an exception when used as a root type")
    void shouldThrowExceptionWhenUsedAsRoot() {
        String json = "{\"value\":123,\"present\":true}";

        assertThatThrownBy(() -> objectMapper.readValue(json, Update.class))
                .isInstanceOf(InvalidUpdateUsageException.class);
    }

    @ParameterizedTest
    @SuppressWarnings("unchecked")
    @DisplayName("deserializes various JSON payloads into Update fields")
    @MethodSource("getDummyJsons")
    void deserializeJsonsToUpdate(String json, Boolean p1, Boolean p2, Boolean p3, String v1, String v2, String v3) throws JsonProcessingException {
        // when
        UpdateTestDTO dto = objectMapper.readValue(json, UpdateTestDTO.class);
        System.out.println(dto.toString());

        assertUpdate(dto.getT1(), p1, v1);
        assertUpdate(dto.getT2(), p2, v2);
        assertUpdate(dto.getT3(), p3, v3);

    }

    private <T> void assertUpdate(Update<T> update, boolean expectedPresent, T expectedValue) {
        assertThat(update).isNotNull();
        assertThat(update.isPresent()).isEqualTo(expectedPresent);
        assertThat(update.getValue()).isEqualTo(expectedValue);
    }

    static Stream<Arguments> getDummyJsons() {
        return Stream.of(
                Arguments.of("{\"t1\":\"value1\"}", true, false, false, "value1", null, null),
                Arguments.of("{\"t2\":\"value2\"}", false, true, false, null, "value2", null),
                Arguments.of("{\"t3\":\"null\"}", false, false, true, null, null, "null"),
                Arguments.of("{\"t1\":\"value1\", \"t2\":null}", true, true, false, "value1", null, null),
                Arguments.of("{\"t1\":\"value1\", \"t3\":\"value3\"}", true, false, true, "value1", null, "value3"),
                Arguments.of("{\"t2\":\"null\", \"t3\":null}", false, true, true, null, "null", null),
                Arguments.of("{\"t1\":null, \"t2\":\"null\", \"t3\":\"value3\"}", true, true, true, null, "null", "value3"),
                Arguments.of("{}", false, false, false, null, null, null)
        );
    }
}
