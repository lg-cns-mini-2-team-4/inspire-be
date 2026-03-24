package com.inspire.auth.infrastructure.store;

import com.inspire.auth.domain.enums.TokenType;
import com.inspire.auth.domain.vo.OAuth2UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository("oneTimeTokenStore")
@RequiredArgsConstructor
public class OneTimeTokenStore implements RedisStore<String, OAuth2UserVO> {

    private final RedisTemplate<String, OAuth2UserVO> oAuth2UserVORedisTemplate;
    private static final TokenType TOKEN_TYPE = TokenType.ONETIME;

    @Override
    public void save(String token, OAuth2UserVO oAuth2UserVO, Duration ttl) {
        oAuth2UserVORedisTemplate.opsForValue().set(addPrefix(token), oAuth2UserVO, ttl);
    }

    @Override
    public void delete(String token) {
        oAuth2UserVORedisTemplate.delete(addPrefix(token));
    }

    @Override
    public boolean exists(String token) {
        return oAuth2UserVORedisTemplate.hasKey(addPrefix(token));
    }

    @Override
    public Optional<OAuth2UserVO> get(String token) {
        return Optional.ofNullable(oAuth2UserVORedisTemplate.opsForValue().get(addPrefix(token)));
    }

    @Override
    public Optional<OAuth2UserVO> getAndDelete(String token) {
        return Optional.ofNullable(oAuth2UserVORedisTemplate.opsForValue().getAndDelete(addPrefix(token)));
    }

    private String addPrefix(String token) {
        return TOKEN_TYPE.getPrefix() + ":" + token;
    }
}
