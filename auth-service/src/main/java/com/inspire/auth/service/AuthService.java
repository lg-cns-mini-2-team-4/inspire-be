package com.inspire.auth.service;

import com.inspire.auth.domain.dto.LoginRequest;
import com.inspire.auth.domain.dto.LoginResponse;
import com.inspire.auth.domain.dto.SignupRequest;

public interface AuthService {
    void signup(SignupRequest request);
    LoginResponse login(LoginRequest request);
}
