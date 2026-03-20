package com.inspire.auth.security.handler;

import com.inspire.auth.infrastructure.entity.UserCredentials;
import com.inspire.auth.infrastructure.enums.Provider;
import com.inspire.auth.infrastructure.repository.UserCredentialsRepository;
import com.inspire.auth.infrastructure.store.RedisStore;
import com.inspire.auth.security.principal.InspireOAuth2User;
import com.inspire.common.cookie.servlet.CookieUtils;
import com.inspire.common.jwt.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${app.frontend.url.registration}")
    private String REGISTRATION_URL;
    @Value("${app.frontend.url.login-success}")
    private String LOGIN_SUCCESS_URL;
    private final UserCredentialsRepository credentialsRepository;
    private final RedisStore<Long, String> redisStore;
    //  private final OneTimeTokenService oneTimeTokenService;
    private final JwtUtils jwtUtils;
    private final CookieUtils cookieUtils;
    private static final String COOKIE_NAME = "inspire_refresh";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        InspireOAuth2User oAuth2User = (InspireOAuth2User) authentication.getPrincipal();
        Provider provider = Provider.valueOf(oAuth2User.getProvider().toUpperCase());
        String externalId = oAuth2User.getExternalId();
        String email = oAuth2User.getEmail();

        UserCredentials credentials = credentialsRepository.findByProviderAndExternalId(provider, externalId)
                .orElseGet(() -> UserCredentials.builder()
                        .email(email)
                        .provider(provider)
                        .externalId(externalId)
                        .build());

        Long userId = credentials.getUserId();
        String token = jwtUtils.createRefreshToken(userId);
        long expires = jwtUtils.getRefreshExpiresInSeconds();
        redisStore.save(userId, token, Duration.ofSeconds(expires));

        cookieUtils.addCookie(response, COOKIE_NAME, token, "/", (int) expires, true);

        response.sendRedirect(LOGIN_SUCCESS_URL);
    }
}
