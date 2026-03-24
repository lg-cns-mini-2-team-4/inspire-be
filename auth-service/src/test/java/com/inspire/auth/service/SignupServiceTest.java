package com.inspire.auth.service;

import com.inspire.auth.domain.dto.request.SignupRequest;
import com.inspire.auth.exception.AuthErrorCode;
import com.inspire.auth.exception.AuthException;
import com.inspire.auth.infrastructure.client.UserClient;
import com.inspire.auth.infrastructure.entity.UserCredentials;
import com.inspire.auth.infrastructure.enums.Provider;
import com.inspire.auth.infrastructure.repository.UserCredentialsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
class SignupServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserCredentialsRepository userRepository;

    @MockitoBean
    private UserClient userClient; // Feign mock

    @Test
    void signup() {

        // given
        SignupRequest request = new SignupRequest("test", "test@test.com", null, "123456789a!!");

        // Feign 호출 실패하도록 설정
        doThrow(new AuthException(AuthErrorCode.FEIGN_CLIENT_ERROR))
                .when(userClient)
                .createUserProfile(any());

        // when
        try {
            authService.signup(request);
        } catch (Exception ignored) {
        }

        // then
        Optional<UserCredentials> user =
                userRepository.findByEmailAndProvider(
                        "test@test.com", Provider.INSPIRE);

        // 🔥 핵심 검증
        assertThat(user).isEmpty(); // 롤백됐으면 없어야 함
    }
}