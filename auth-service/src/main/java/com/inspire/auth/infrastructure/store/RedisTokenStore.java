package com.inspire.auth.infrastructure.store;

import com.inspire.auth.domain.enums.TokenType;

import java.time.Duration;

public interface RedisTokenStore {
    void save(TokenType tokenType, String token, String payload, Duration ttl);
    void delete(TokenType tokenType, String token);
}
