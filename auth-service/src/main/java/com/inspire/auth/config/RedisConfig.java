package com.inspire.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inspire.auth.domain.vo.OAuth2UserVO;
import com.inspire.auth.security.handler.OAuth2AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.jackson2.SimpleGrantedAuthorityMixin;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

@Configuration(proxyBeanMethods = false)
public class RedisConfig {

    private final String host;
    private final int port;
    private final String password;

    public RedisConfig(@Value("${spring.data.redis.host}") String host,
                       @Value("${spring.data.redis.port}") int port,
                       @Value("${spring.data.redis.password}") String password) {
        this.host = host;
        this.port = port;
        this.password = password;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        config.setPassword(password);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, OAuth2UserVO> oAuth2RedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, OAuth2UserVO> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(OAuth2UserVO.class));
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, OAuth2AuthorizationRequest> oAuth2RequestRedisTemplate(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
        RedisTemplate<String, OAuth2AuthorizationRequest> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, OAuth2AuthorizationRequest.class));
        return redisTemplate;
    }


}
