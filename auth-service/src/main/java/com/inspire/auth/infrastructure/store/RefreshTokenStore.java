package com.inspire.auth.infrastructure.store;

import com.inspire.auth.domain.enums.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository("refreshTokenStore")
@RequiredArgsConstructor
public class RefreshTokenStore implements RedisTokenStore<String> {

    private final RedisTemplate<String, String> redisTemplate;
    private static final TokenType TOKEN_TYPE = TokenType.REFRESH;

    @Override
    public void save(String token, String userId, Duration ttl) {
        redisTemplate.opsForValue().set(addPrefix(token), userId, ttl);
    }

    @Override
    public void delete(String token) {
        redisTemplate.delete(addPrefix(token));
    }

    @Override
    public boolean exists(String token) {
        return redisTemplate.hasKey(addPrefix(token));
    }

    @Override
    public Optional<String> get(String token) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(addPrefix(token)));
    }

    @Override
    public Optional<String> getAndDelete(String token) {
        return Optional.ofNullable(redisTemplate.opsForValue().getAndDelete(addPrefix(token)));
    }

    private String addPrefix(String token) {
        return TOKEN_TYPE.getPrefix() + ":" + token;
    }
}
