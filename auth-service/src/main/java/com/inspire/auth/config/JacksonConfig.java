package com.inspire.auth.config;

import com.inspire.auth.security.oauth.jackson.RedisOAuth2AuthorizationRequestMixin;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

@Configuration(proxyBeanMethods = false)
public class JacksonConfig {

    @Bean
    Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.mixIn(OAuth2AuthorizationRequest.class, RedisOAuth2AuthorizationRequestMixin.class);
    }
}
