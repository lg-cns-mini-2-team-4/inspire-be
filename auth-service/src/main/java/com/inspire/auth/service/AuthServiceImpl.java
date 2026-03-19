package com.inspire.auth.service;

import com.inspire.auth.domain.dto.response.AccessTokenDTO;
import com.inspire.common.jwt.JwtUtils;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    @Override
    public AccessTokenDTO reissue(HttpServletResponse response, String refreshToken) {

        String accessToken = refreshTokenService.generateAccessFromRefreshToken(response, refreshToken);
        Long expires = refreshTokenService.getAccessExpiresInSeconds();

        return new AccessTokenDTO(accessToken, expires);
    }
}
