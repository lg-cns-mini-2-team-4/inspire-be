package com.inspire.common.binding.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.inspire.common.binding.Update;
import com.inspire.common.binding.UpdateDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Auto-configuration for the {@link Jackson2ObjectMapperBuilderCustomizer} with a {@link UpdateDeserializer}.
 *
 * <p>
 * This configuration registers a {@link Jackson2ObjectMapperBuilderCustomizer} bean and adds {@link UpdateDeserializer},
 * so that it can be applied to a Spring-based application.
 *
 * <p>
 * If a {@link LocalValidatorFactoryBean LocalValidatorFactoryBean}
 * is present in the Spring application context,
 * Bean validation will also be applied to the {@link Update} type.
 *
 * <hr>
 *
 * {@link UpdateDeserializer}와 함께, {@link Jackson2ObjectMapperBuilderCustomizer}를 위한 자동 환경 설정.
 *
 * <p>
 * 스프링 기반의 애플리케이션에서 작동할 수 있도록, {@link UpdateDeserializer}를 추가한
 * {@link Jackson2ObjectMapperBuilderCustomizer} 빈을 등록합니다.
 *
 * <p>
 * 스프링 애플리케이션 컨텍스트에 {@link LocalValidatorFactoryBean LocalValidatorFactoryBean}이
 * 존재한다면, {@link Update} 타입에 대한 Bean validation이 가능합니다.
 *
 * <hr>
 *
 * @author Wooseong Urm
 * @since 1.0.0
 * @see Jackson2ObjectMapperBuilderCustomizer
 */
@Configuration(proxyBeanMethods = false)
public class UpdateAutoConfiguration {

    /**
     * Creates a {@link Jackson2ObjectMapperBuilderCustomizer} bean if no bean is present.
     *
     * <hr>
     *
     * {@link Jackson2ObjectMapperBuilderCustomizer} bean이 등록되지 않은 경우 생성합니다.
     *
     * <hr>
     *
     * @return a {@link Jackson2ObjectMapperBuilderCustomizer} instance
     * @see Jackson2ObjectMapperBuilderCustomizer
     */
    @Bean
    @ConditionalOnMissingBean(name = "inspireUpdateJacksonMapperCustomizer")
    public Jackson2ObjectMapperBuilderCustomizer inspireUpdateJacksonMapperCustomizer() {
        SimpleModule updateModule = new SimpleModule();
        updateModule.addDeserializer(Update.class, new UpdateDeserializer<>());

        return builder -> builder.modules(updateModule);
    }

}
