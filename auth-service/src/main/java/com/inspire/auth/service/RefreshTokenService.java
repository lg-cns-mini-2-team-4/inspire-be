package com.inspire.auth.service;

import com.inspire.auth.exception.AuthErrorCode;
import com.inspire.auth.exception.AuthException;
import com.inspire.auth.infrastructure.store.RedisTokenStore;
import com.inspire.common.cookie.servlet.CookieUtils;
import com.inspire.common.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.inspire.common.jwt.exception.JwtValidationException;

import java.time.Duration;
import java.util.Objects;
import java.util.Set;

@Service
@Getter
public class RefreshTokenService {
    private final RedisTokenStore<String> refreshTokenStore;
    private final JwtUtils jwtUtils;
    private final CookieUtils cookieUtils;
    private final String domain;
    private final long refreshExpiresInSeconds;
    private final long accessExpiresInSeconds;
    private static final String COOKIE_NAME = "inspire_refresh";

    public RefreshTokenService(@Qualifier("refreshTokenStore") RedisTokenStore<String> refreshTokenStore,
                               JwtUtils jwtUtils, CookieUtils cookieUtils,
                               @Value("${cookie.domain:#{null}}") String domain) {
        this.refreshTokenStore = refreshTokenStore;
        this.jwtUtils = jwtUtils;
        this.cookieUtils = cookieUtils;
        this.domain = domain;
        this.accessExpiresInSeconds = jwtUtils.getAccessExpiresInSeconds();
        this.refreshExpiresInSeconds = jwtUtils.getRefreshExpiresInSeconds();
    }

    public void saveRefreshTokenAndAddCookie(HttpServletResponse response, Long userId) {
        String token = jwtUtils.createRefreshToken(userId);
        refreshTokenStore.save(token, String.valueOf(userId), Duration.ofSeconds(refreshExpiresInSeconds));
        cookieUtils.addCookie(response, COOKIE_NAME, token, domain, "/", (int) refreshExpiresInSeconds, true);
    }

    /**
     *
     * @param refreshToken
     * @throws JwtValidationException
     * @throws AuthException
     */
    public String generateAccessFromRefreshToken(HttpServletResponse response, String refreshToken) {

        // jwt 자체가 만료, 위조되면 JwtValidationException 발생
        Long userId = jwtUtils.getUserIdFromRefreshToken(refreshToken);

        // jwt는 통과, 근데 redis에 없으면?
        Long userIdInRedis = refreshTokenStore.get(refreshToken)
                .map(Long::parseLong)
                .orElseThrow(() -> new AuthException(AuthErrorCode.REFRESH_NOT_IN_REDIS));

        if(!Objects.equals(userId, userIdInRedis)) {
            throw new AuthException(AuthErrorCode.REFRESH_CONFLICT_WITH_REDIS);
        }

        refreshTokenStore.delete(refreshToken);

        String accessToken = jwtUtils.createAccessToken(userId, Set.of("ROLE_USER"));
        String newRefreshToken = jwtUtils.createRefreshToken(userId);

        refreshTokenStore.save(newRefreshToken, String.valueOf(userId), Duration.ofSeconds(refreshExpiresInSeconds));
        cookieUtils.addCookie(response, COOKIE_NAME, newRefreshToken, domain, "/", (int) refreshExpiresInSeconds, true);

        return accessToken;
    }

}
