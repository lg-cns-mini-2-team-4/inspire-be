package com.inspire.auth.security.handler;

import com.inspire.auth.domain.vo.OAuth2UserVO;
import com.inspire.auth.infrastructure.entity.UserCredentials;
import com.inspire.auth.infrastructure.enums.OAuth2Provider;
import com.inspire.auth.infrastructure.repository.UserCredentialsRepository;
import com.inspire.auth.security.principal.InspireOAuth2User;
import com.inspire.auth.service.OneTimeTokenService;
import com.inspire.auth.service.RefreshTokenService;
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
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${app.frontend.url.registration}")
    private String REGISTRATION_URL;
    @Value("${app.frontend.url.login-success}")
    private String LOGIN_SUCCESS_URL;
    private final UserCredentialsRepository credentialsRepository;
    private final RefreshTokenService refreshTokenService;
    private final OneTimeTokenService oneTimeTokenService;
    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        InspireOAuth2User oAuth2User = (InspireOAuth2User) authentication.getPrincipal();
        OAuth2Provider provider = OAuth2Provider.valueOf(oAuth2User.getProvider().toUpperCase());
        String externalId = oAuth2User.getExternalId();
        String email = oAuth2User.getEmail();

        Optional<UserCredentials> credentials = credentialsRepository.findByProviderAndExternalId(provider, externalId);

        if (credentials.isPresent()) {
            // JWT 토큰 발급 및 redirect
            Long userId = credentials.get().getUserId();
            String token = jwtUtils.createRefreshToken(userId);
            refreshTokenService.saveRefreshTokenAndCookie(response, userId, token, jwtUtils.getRefreshExpiresInSeconds());

            response.sendRedirect(LOGIN_SUCCESS_URL);
        } else {
            OAuth2UserVO oAuth2UserVo = new OAuth2UserVO(
                    oAuth2User.getExternalId(),
                    oAuth2User.getExternalName(),
                    oAuth2User.getEmail(),
                    oAuth2User.getProvider()
            );
            // onetime token 발급 및 redirect
            oneTimeTokenService.saveOneTimeTokenAndAddCookie(response, oAuth2UserVo);
            response.sendRedirect(REGISTRATION_URL);
            // 이메일이 존재하면? 이건 지금은 처리하지 말자 ㅇㅇ 시간 없음
        }
    }
}
