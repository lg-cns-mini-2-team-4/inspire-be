package com.inspire.auth.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration(proxyBeanMethods = false)
public class SwaggerConfig {

    @Bean
    @Profile("!prod")
    public OpenAPI customOpenAPI() {
        Components components = new Components()
                .addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))

                .addSecuritySchemes("cookieAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                                .name("inspire_refresh"));
        return new OpenAPI()
                .components(components)
                .info(new Info()
                        .title("Auth Service API")
                        .version("v1.0")
                        .description("API documentation for Auth Service"));
    }
}
