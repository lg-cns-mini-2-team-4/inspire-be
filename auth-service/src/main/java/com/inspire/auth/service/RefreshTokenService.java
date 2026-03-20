package com.inspire.auth.service;

import com.inspire.auth.exception.AuthErrorCode;
import com.inspire.auth.exception.AuthException;
import com.inspire.auth.infrastructure.store.RedisStore;
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
import java.util.Optional;
import java.util.Set;

@Service
@Getter
public class RefreshTokenService {
    private final RedisStore<Long, String> refreshTokenStore;
    private final CookieUtils cookieUtils;
    private final String domain;
    private static final String COOKIE_NAME = "inspire_refresh";

    public RefreshTokenService(@Qualifier("refreshTokenStore") RedisStore<Long, String> refreshTokenStore,
                               CookieUtils cookieUtils,
                               @Value("${cookie.domain:#{null}}") String domain) {
        this.refreshTokenStore = refreshTokenStore;
        this.cookieUtils = cookieUtils;
        this.domain = domain;
    }

    public void saveRefreshTokenAndCookie(HttpServletResponse res, Long userId, String token, long refreshExpiresInSeconds) {
        refreshTokenStore.save(userId, token, Duration.ofSeconds(refreshExpiresInSeconds));
        cookieUtils.addCookie(res, COOKIE_NAME, token, domain, "/", (int) refreshExpiresInSeconds, true);
    }


    public String getSavedToken(Long userId) {
        return refreshTokenStore.get(userId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.REFRESH_NOT_IN_REDIS));
    }

    public void clearRefreshTokenAndCookie(HttpServletResponse res, Long userId) {

        refreshTokenStore.delete(userId);
        cookieUtils.deleteCookie(res, COOKIE_NAME, domain, "/");
    }
}
