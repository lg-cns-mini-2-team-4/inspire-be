package com.inspire.schedule_service.schedule.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Components components = new Components()
                .addParameters("X-User-Id", new Parameter()
                        .in("header")
                        .name("X-User-Id")
                        .description("사용자 ID")
                        .required(true)
                );

        return new OpenAPI()
                .components(components)
                .info(new Info()
                        .title("Schedule Service API")
                        .version("v1.0")
                        .description("API documentation for Schedule Service"));
    }
}
