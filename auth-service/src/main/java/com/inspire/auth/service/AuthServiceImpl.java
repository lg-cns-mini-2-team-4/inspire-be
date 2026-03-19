package com.inspire.auth.service;

import com.inspire.auth.domain.dto.LoginRequest;
import com.inspire.auth.domain.dto.LoginResponse;
import com.inspire.auth.domain.dto.SignupRequest;
import com.inspire.auth.domain.enums.TokenType;
import com.inspire.auth.exception.AuthErrorCode;
import com.inspire.auth.exception.AuthException;
import com.inspire.auth.infrastructure.client.UserClient;
import com.inspire.auth.infrastructure.client.dto.UserProfileCreateRequest;
import com.inspire.auth.infrastructure.entity.UserCredentials;
import com.inspire.auth.infrastructure.enums.Provider;
import com.inspire.auth.infrastructure.repository.UserCredentialsRepository;
import com.inspire.auth.infrastructure.store.RedisTokenStore;
import com.inspire.common.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserCredentialsRepository userCredentialsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserClient userClient;
    private final RedisTokenStore redisTokenStore;

    @Override
    @Transactional
    public void signup(SignupRequest request) {
        if (userCredentialsRepository.existsByLoginIdAndProvider(request.getEmail(), Provider.INSPIRE)) {
            throw new AuthException(AuthErrorCode.USER_ALREADY_EXISTS);
        }

        UserCredentials user = UserCredentials.builder()
                .loginId(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .provider(Provider.INSPIRE)
                .build();

        userCredentialsRepository.save(user);

        // Notify user-service to create profile via OpenFeign
        userClient.createUserProfile(
                new UserProfileCreateRequest(user.getUserId(), request.getName(), request.getEmail())
        );
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        UserCredentials user = userCredentialsRepository.findByLoginIdAndProvider(request.getEmail(), Provider.INSPIRE)
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new AuthException(AuthErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtUtils.createAccessToken(user.getUserId(), List.of("ROLE_USER"));
        String refreshToken = jwtUtils.createRefreshToken(user.getUserId());

        // Default TTL for refresh token to 14 days
        redisTokenStore.save(TokenType.REFRESH, refreshToken, String.valueOf(user.getUserId()), Duration.ofDays(14));

        return new LoginResponse(user.getUserId(), accessToken, refreshToken);
    }
}
