package com.inspire.auth.service;

import com.inspire.auth.domain.vo.OAuth2UserVO;
import com.inspire.auth.infrastructure.store.RedisStore;
import com.inspire.common.cookie.servlet.CookieUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
public class OneTimeTokenService {

    private final RedisStore<String, OAuth2UserVO> oneTimeTokenStore;
    private final CookieUtils cookieUtils;
    private final long onetimeExpiresInSeconds;
    private static final String COOKIE_NAME = "inspire_onetime";

    public OneTimeTokenService(@Qualifier("oneTimeTokenStore") RedisStore<String, OAuth2UserVO> oneTimeTokenStore,
                               CookieUtils cookieUtils) {
        this.oneTimeTokenStore = oneTimeTokenStore;
        this.cookieUtils = cookieUtils;
        this.onetimeExpiresInSeconds = 300;
    }

    public void saveOneTimeTokenAndAddCookie(HttpServletResponse response, OAuth2UserVO oAuth2UserVO) {
        String token = generateOneTimeToken();
        oneTimeTokenStore.save(token, oAuth2UserVO, Duration.ofSeconds(onetimeExpiresInSeconds));
        cookieUtils.addCookie(response, COOKIE_NAME, token, "/", (int) onetimeExpiresInSeconds, true);
    }

    public Optional<OAuth2UserVO> tempGetOAuth2VO(String token) {
        return oneTimeTokenStore.getAndDelete(token);
    }

    private String generateOneTimeToken() {
        return UUID.randomUUID().toString();
    }

}
