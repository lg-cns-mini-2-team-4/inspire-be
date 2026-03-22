package com.inspire.common.binding.config;

import com.inspire.common.binding.Update;
import com.inspire.common.binding.validation.constraints.NotAbsent;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Tests for UpdateAutoConfiguration")
public class UpdateAutoConfigurationTest {

    @NoArgsConstructor
    @Getter
    @Setter
    static class TestDto {
        @NotAbsent
        private Update<@NotNull String> t;
    }

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(UpdateAutoConfiguration.class));

    @Test
    @DisplayName("should fail if field value is traditional null")
    void shouldThrowValidationException() {
        contextRunner.withBean(LocalValidatorFactoryBean.class, LocalValidatorFactoryBean::new)
                .run(context -> {
                    Validator validator = context.getBean(Validator.class);
                    TestDto dto = new TestDto();
                    dto.setT(null);

                    assertThatThrownBy(() -> validator.validate(dto))
                            .isInstanceOf(ValidationException.class);
                });
    }
}
