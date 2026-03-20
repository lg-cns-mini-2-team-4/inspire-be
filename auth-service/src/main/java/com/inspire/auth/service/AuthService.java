package com.inspire.auth.service;

import com.inspire.auth.domain.dto.request.LoginRequest;
import com.inspire.auth.domain.dto.response.SignupRequest;

import com.inspire.auth.domain.dto.result.TokenResult;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    void signup(SignupRequest request);
    TokenResult login(LoginRequest request);
    TokenResult reissue(String refreshToken);
    void logout(HttpServletResponse res, Long userId, String refreshToken);
}
