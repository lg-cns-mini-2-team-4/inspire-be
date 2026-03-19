package com.inspire.auth.infrastructure.store;

import com.inspire.auth.domain.enums.TokenType;

import java.time.Duration;
import java.util.Optional;

public interface RedisTokenStore<T> {
    void save(String key, T payload, Duration ttl);
    void delete(String key);
    boolean exists(String key);
    Optional<T> get(String key);
    Optional<T> getAndDelete(String key);
}
