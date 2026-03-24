package com.inspire.auth.infrastructure.store;

import com.inspire.auth.domain.enums.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository("refreshTokenStore")
@RequiredArgsConstructor
public class RefreshTokenStore implements RedisStore<Long, String> {

    private final RedisTemplate<String, String> redisTemplate;
    private static final TokenType TOKEN_TYPE = TokenType.REFRESH;

    @Override
    public void save(Long userId, String token, Duration ttl) {
        redisTemplate.opsForValue().set(addPrefix(userId), token, ttl);
    }

    @Override
    public void delete(Long userId) {
        redisTemplate.delete(addPrefix(userId));
    }

    @Override
    public boolean exists(Long userId) {
        return redisTemplate.hasKey(addPrefix(userId));
    }

    @Override
    public Optional<String> get(Long userId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(addPrefix(userId)));
    }

    @Override
    public Optional<String> getAndDelete(Long userId) {
        return Optional.ofNullable(redisTemplate.opsForValue().getAndDelete(addPrefix(userId)));
    }

    private String addPrefix(Long userId) {
        return TOKEN_TYPE.getPrefix() + ":" + userId;
    }
}
