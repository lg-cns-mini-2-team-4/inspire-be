package com.inspire.auth.controller;

import com.inspire.auth.domain.dto.request.LoginRequest;
import com.inspire.auth.domain.dto.request.SignupRequest;
import com.inspire.auth.domain.dto.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface AuthApiSpecification {
    @Operation(summary = "회원가입")
    ResponseEntity<Void> signup(@RequestBody SignupRequest request);

    @Operation(summary = "로그인")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    ResponseEntity<TokenResponse> login(HttpServletResponse res, @RequestBody LoginRequest request);

    @Operation(summary = "로그아웃", description = "서버 측 세션(Refresh Token)을 무효화하고 쿠키를 초기화합니다.")
    @ApiResponse(responseCode = "204", description = "로그아웃 성공")
    ResponseEntity<Void> logout(HttpServletResponse res,
                                       @RequestHeader(name = "X-User-Id", required = false) Long userId,
                                       @CookieValue(name = "inspire_refresh", required = false) String refreshToken);

    @Operation(summary = "토큰 재발급", description = "테스트")
    ResponseEntity<TokenResponse> reissue(HttpServletResponse res, @CookieValue(name = "inspire_refresh") String refreshToken);

    @Operation(summary = "임시 oauth 회원가입", description = "테스트용")
    ResponseEntity<TokenResponse> tempOAuth2Signup(HttpServletResponse res, @CookieValue(name = "inspire_onetime") String oneTimeToken);
}
