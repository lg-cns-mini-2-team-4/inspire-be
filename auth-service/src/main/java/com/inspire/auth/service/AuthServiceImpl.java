package com.inspire.auth.service;

import com.inspire.auth.domain.dto.request.UserLoginDTO;
import com.inspire.auth.domain.dto.response.AccessTokenDTO;
import com.inspire.auth.exception.AuthErrorCode;
import com.inspire.auth.exception.AuthException;
import com.inspire.auth.infrastructure.entity.UserCredentials;
import com.inspire.auth.infrastructure.repository.UserCredentialsRepository;
import com.inspire.common.jwt.JwtUtils;
import jakarta.persistence.Access;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final UserCredentialsRepository credentialsRepository;

    @Override
    @Transactional(readOnly = true)
    public AccessTokenDTO login(HttpServletResponse res, UserLoginDTO userLoginDTO) {
        String email = userLoginDTO.getEmail();
        String password = userLoginDTO.getPassword();
        UserCredentials credentials = credentialsRepository.findByEmail(email)
                .orElse(null);

        if (credentials == null || !passwordEncoder.matches(password, credentials.getPasswordHash())) {
            throw new AuthException(AuthErrorCode.INVALID_CREDENTIALS);
        }

        Long userId = credentials.getUserId();
        String accessToken = jwtUtils.createAccessToken(userId, Set.of("ROLE_USER"));
        String newRefreshToken = jwtUtils.createRefreshToken(userId);

        refreshTokenService.saveRefreshTokenAndCookie(res, userId, newRefreshToken, jwtUtils.getRefreshExpiresInSeconds());

        return new AccessTokenDTO(accessToken, jwtUtils.getAccessExpiresInSeconds());
    }

    @Override
    public AccessTokenDTO reissue(HttpServletResponse res, String refreshToken) {
        // jwt 자체가 만료, 위조되면 JwtValidationException 발생
        Long userId = jwtUtils.getUserIdFromRefreshToken(refreshToken);

        // 없으면 AuthException (TOKEN_NOT_IN_REDIS) 발생
        String savedToken = refreshTokenService.getSavedToken(userId);

        // 다르면 AuthException (TOKEN_CONFLICT) 발생
        if (!refreshToken.equals(savedToken)) {
            throw new AuthException(AuthErrorCode.TOKEN_CONFLICT);
        }

        String accessToken = jwtUtils.createAccessToken(userId, Set.of("ROLE_USER"));
        String newRefreshToken = jwtUtils.createRefreshToken(userId);

        refreshTokenService.saveRefreshTokenAndCookie(res, userId, newRefreshToken, jwtUtils.getRefreshExpiresInSeconds());

        return new AccessTokenDTO(accessToken, jwtUtils.getAccessExpiresInSeconds());
    }

    @Override
    public void logout(HttpServletResponse res, Long userId, String refreshToken) {
        // 둘 중 하나라도 없으면 AuthException (MISSING_AUTH_TOKEN) 바생
        if (userId == null || refreshToken == null) {
            throw new AuthException(AuthErrorCode.MISSING_AUTH_TOKEN);
        }

        // jwt 자체가 만료, 위조되면 JwtValidationException 발생
        Long tokenUserId = jwtUtils.getUserIdFromRefreshToken(refreshToken);

        // 다르면 AuthException (TOKEN_CONFLICT) 발생
        if (!Objects.equals(userId, tokenUserId)) {
            throw new AuthException(AuthErrorCode.TOKEN_CONFLICT);
        }

        refreshTokenService.clearRefreshTokenAndCookie(res, userId);
    }
}
