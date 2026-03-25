package com.inspire.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inspire.auth.domain.dto.request.LoginRequest;
import com.inspire.auth.domain.dto.request.SignupRequest;
import com.inspire.auth.domain.dto.result.TokenResult;
import com.inspire.auth.domain.vo.OAuth2UserVO;
import com.inspire.auth.exception.AuthErrorCode;
import com.inspire.auth.exception.AuthException;
import com.inspire.auth.infrastructure.client.UserClient;
import com.inspire.auth.infrastructure.client.dto.UserProfileCreateRequest;
import com.inspire.auth.infrastructure.entity.UserCredentials;
import com.inspire.auth.infrastructure.enums.Provider;
import com.inspire.auth.infrastructure.repository.UserCredentialsRepository;
import com.inspire.auth.infrastructure.store.RedisStore;
import com.inspire.common.jwt.JwtUtils;
import feign.FeignException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserCredentialsRepository userCredentialsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserClient userClient;
    private final RedisStore<Long, String> redisStore;
    private final OneTimeTokenService oneTimeTokenService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void signup(SignupRequest request) {
        if (userCredentialsRepository.existsByEmailAndProvider(request.getEmail(), Provider.INSPIRE)) {
            throw new AuthException(AuthErrorCode.USER_ALREADY_EXISTS);
        }

        UserCredentials user = UserCredentials.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .provider(Provider.INSPIRE)
                .build();

        userCredentialsRepository.save(user);

        try {
            userClient.createUserProfile(UserProfileCreateRequest.builder()
                    .id(user.getUserId())
                    .name(request.getName())
                    .phone(request.getPhone())
                    .email(request.getEmail())
                    .build()
            );
        } catch (FeignException e) {
            log.debug("**************************************");
            log.debug(e.getMessage());
            log.debug("**************************************");
            log.debug(e.request().toString());
            log.debug("**************************************");
            log.debug(e.contentUTF8());
            throw new AuthException(AuthErrorCode.FEIGN_CLIENT_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TokenResult login(LoginRequest request) {
        UserCredentials user = userCredentialsRepository.findByEmailAndProvider(request.getEmail(), Provider.INSPIRE)
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND));

        log.info("유저확인 성공");
        System.out.println("유저확인 성공");
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new AuthException(AuthErrorCode.INVALID_PASSWORD);
        }

        log.info("패스워드 확인 성공");
        System.out.println("패스워드 확인 성공");

        String accessToken = jwtUtils.createAccessToken(user.getUserId(), List.of("ROLE_USER"));

        log.info("access 생성 성공");
        System.out.println("access 생성 성공");
        String refreshToken = jwtUtils.createRefreshToken(user.getUserId());

        log.info("refresh 생성 성공");
        System.out.println("refresh 생성 성공");

        // Default TTL for refresh token to 14 days
        redisStore.save(user.getUserId(), refreshToken, Duration.ofSeconds(jwtUtils.getRefreshExpiresInSeconds()));

        log.info("redis 저장 성공");
        System.out.println("redis 저장 성공");

        return new TokenResult(accessToken, refreshToken);
    }

    @Override
    public TokenResult reissue(String refreshToken) {
        // jwt 자체가 만료, 위조되면 JwtValidationException 발생
        Long userId = jwtUtils.getUserIdFromRefreshToken(refreshToken);

        // 없으면 AuthException (TOKEN_NOT_IN_REDIS) 발생
        String savedToken = redisStore.get(userId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.REFRESH_NOT_IN_REDIS));

        // 다르면 AuthException (TOKEN_CONFLICT) 발생
        if (!refreshToken.equals(savedToken)) {
            throw new AuthException(AuthErrorCode.TOKEN_CONFLICT);
        }

        String accessToken = jwtUtils.createAccessToken(userId, List.of("ROLE_USER"));
        String newRefreshToken = jwtUtils.createRefreshToken(userId);

        redisStore.save(userId, newRefreshToken, Duration.ofSeconds(jwtUtils.getRefreshExpiresInSeconds()));

        return new TokenResult(accessToken, newRefreshToken);
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

        redisStore.delete(userId);
    }

    @Override
    @Transactional
    public TokenResult tempOAuth2Signup(String onetimeToken) {
        OAuth2UserVO vo = oneTimeTokenService.tempGetOAuth2VO(onetimeToken)
                .orElseThrow(() -> new AuthException(AuthErrorCode.ONETIME_NOT_IN_REDIS));

        UserCredentials user = UserCredentials.builder()
                .email(vo.getEmail())
                .provider(Provider.valueOf(vo.getProvider().toUpperCase()))
                .externalId(vo.getExternalId())
                .build();

        userCredentialsRepository.save(user);

        try {
            userClient.createUserProfile(UserProfileCreateRequest.builder()
                    .id(user.getUserId())
                    .name(vo.getName())
                    .email(vo.getEmail())
                    .build()
            );
        } catch (FeignException e) {
            throw new AuthException(AuthErrorCode.FEIGN_CLIENT_ERROR);
        }

        Long userId = user.getUserId();
        String accessToken = jwtUtils.createAccessToken(userId, List.of("ROLE_USER"));
        String refreshToken = jwtUtils.createRefreshToken(userId);

        redisStore.save(userId, refreshToken, Duration.ofSeconds(jwtUtils.getRefreshExpiresInSeconds()));

        return new TokenResult(accessToken, refreshToken);
    }
}

