package com.inspire.auth.controller;

import com.inspire.auth.domain.dto.response.TokenResponse;
import com.inspire.auth.domain.dto.result.TokenResult;
import com.inspire.auth.service.AuthService;
import com.inspire.common.jwt.config.JwtProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.inspire.auth.domain.dto.request.LoginRequest;
import com.inspire.auth.domain.dto.request.SignupRequest;
import com.inspire.auth.exception.AuthErrorCode;
import com.inspire.auth.exception.AuthException;
import com.inspire.common.cookie.servlet.CookieUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@Tag(name = "auth", description = "Authentication APIs")
public class AuthController implements AuthApiSpecification {

    private static final String COOKIE_NAME = "inspire_refresh";
    private final AuthService authService;
    private final CookieUtils cookieUtils;
    private final Integer accessExpires;
    private final Integer refreshExpires;

    public AuthController(AuthService authService, CookieUtils cookieUtils, JwtProperties jwtProperties) {
        this.authService = authService;
        this.cookieUtils = cookieUtils;
        this.accessExpires = (int) (jwtProperties.getAccess().getExpires() / 1000);
        this.refreshExpires = (int) (jwtProperties.getRefresh().getExpires() / 1000);
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    public ResponseEntity<Void> signup(@RequestBody @Valid SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    @Operation(summary = "일반 로그인")
    public ResponseEntity<TokenResponse> login(HttpServletResponse res, @RequestBody @Valid LoginRequest request) {

        TokenResult tokenResult = authService.login(request);
        cookieUtils.addCookie(res, COOKIE_NAME, tokenResult.getRefreshToken(), "/", refreshExpires, true);

        return ResponseEntity.ok(new TokenResponse(tokenResult.getAccessToken(), accessExpires));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse res,
                                       @RequestHeader(name = "X-User-Id", required = false) Long userId,
                                       @CookieValue(name = COOKIE_NAME, required = false) String refreshToken) {

        authService.logout(res, userId, refreshToken);
        cookieUtils.deleteCookie(res, COOKIE_NAME, "/");
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(HttpServletResponse res, @CookieValue(name = COOKIE_NAME) String refreshToken) {
        TokenResult tokenResult = authService.reissue(refreshToken);
        cookieUtils.addCookie(res, COOKIE_NAME, tokenResult.getRefreshToken(), "/", refreshExpires, true);
        return ResponseEntity.ok(new TokenResponse(tokenResult.getAccessToken(), accessExpires));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Void> handleAuthException(AuthException e) {
        if (e.getErrorCode() == AuthErrorCode.USER_NOT_FOUND || e.getErrorCode() == AuthErrorCode.INVALID_PASSWORD) {
            return ResponseEntity.status(HttpStatus.SEE_OTHER)
                    .location(URI.create("/login?error"))
                    .build();
        }
        return ResponseEntity.status(e.getErrorCode().getStatus()).build();
    }

    // Keep test endpoints below
    @GetMapping("/test")
    public String test() {
        return "test";
    }

    // 임시 -> 나중엔 회원가입 따로 시킴
    @PostMapping("/oauth/signup")
    public ResponseEntity<Void> tempOAuth2Signup(HttpServletResponse res, @CookieValue(name = "inspire_onetime") String oneTimeToken) {
        authService.tempOAuth2Signup(oneTimeToken);
        cookieUtils.deleteCookie(res, "inspire_onetime", "/");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
