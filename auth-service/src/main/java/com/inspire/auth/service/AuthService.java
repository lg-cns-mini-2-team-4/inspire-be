package com.inspire.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inspire.auth.domain.dto.request.LoginRequest;
import com.inspire.auth.domain.dto.request.SignupRequest;

import com.inspire.auth.domain.dto.result.TokenResult;

import com.inspire.auth.infrastructure.enums.Provider;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {

    void signup(SignupRequest request);
    TokenResult login(LoginRequest request);
    TokenResult reissue(String refreshToken);
    void logout(HttpServletResponse res, Long userId, String refreshToken);
    TokenResult tempOAuth2Signup(String onetimeToken);
}
