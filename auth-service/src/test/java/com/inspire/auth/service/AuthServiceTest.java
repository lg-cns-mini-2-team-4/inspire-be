package com.inspire.auth.service;

import com.inspire.auth.domain.dto.request.LoginRequest;
import com.inspire.auth.domain.dto.result.TokenResult;
import com.inspire.auth.exception.AuthErrorCode;
import com.inspire.auth.exception.AuthException;
import com.inspire.auth.infrastructure.client.UserClient;
import com.inspire.auth.infrastructure.entity.UserCredentials;
import com.inspire.auth.infrastructure.enums.Provider;
import com.inspire.auth.infrastructure.repository.UserCredentialsRepository;
import com.inspire.auth.infrastructure.store.RedisStore;
import com.inspire.common.jwt.JwtUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserCredentialsRepository userCredentialsRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private UserClient userClient;
    @Mock
    private RedisStore<Long, String> redisStore;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    @DisplayName("일반 로그인 성공 - 비밀번호 일치 및 토큰 정상 발급")
    void login_Success() {
        // given
        LoginRequest request = new LoginRequest("test@test.com", "password123");
        UserCredentials user = UserCredentials.builder()
                .userId(200L)
                .email("test@test.com")
                .passwordHash("hashedPassword")
                .provider(Provider.INSPIRE)
                .build();
        
        when(userCredentialsRepository.findByEmailAndProvider("test@test.com", Provider.INSPIRE))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(jwtUtils.createAccessToken(any(), anyList())).thenReturn("access-token");
        when(jwtUtils.createRefreshToken(any())).thenReturn("refresh-token");

        // when
        TokenResult response = authService.login(request);

        // then
        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        verify(redisStore, times(1))
            .save(eq(200L), eq("refresh-token"), eq(Duration.ZERO));
    }
    
    @Test
    @DisplayName("일반 로그인 실패 - 사용자 없음 예외 발생")
    void login_Fail_UserNotFound() {
        // given
        LoginRequest request = new LoginRequest("test@test.com", "password123");
        
        when(userCredentialsRepository.findByEmailAndProvider("test@test.com", Provider.INSPIRE))
                .thenReturn(Optional.empty());

        // when & then
        AuthException exception = assertThrows(AuthException.class, () -> authService.login(request));
        assertEquals(AuthErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
    
    @Test
    @DisplayName("일반 로그인 실패 - 비밀번호 불일치 예외 발생")
    void login_Fail_InvalidPassword() {
        // given
        LoginRequest request = new LoginRequest("test@test.com", "wrongpassword");
        UserCredentials user = UserCredentials.builder()
                .email("test@test.com")
                .passwordHash("hashedPassword")
                .provider(Provider.INSPIRE)
                .build();
        
        when(userCredentialsRepository.findByEmailAndProvider("test@test.com", Provider.INSPIRE))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "hashedPassword")).thenReturn(false);

        // when & then
        AuthException exception = assertThrows(AuthException.class, () -> authService.login(request));
        assertEquals(AuthErrorCode.INVALID_PASSWORD, exception.getErrorCode());
    }
}
