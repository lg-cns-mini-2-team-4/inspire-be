package com.inspire.auth.controller;

import com.inspire.auth.domain.dto.LoginRequest;
import com.inspire.auth.domain.dto.LoginResponse;
import com.inspire.auth.domain.dto.SignupRequest;
import com.inspire.auth.domain.dto.TestDTO;
import com.inspire.auth.exception.AuthErrorCode;
import com.inspire.auth.exception.AuthException;
import com.inspire.auth.service.AuthService;
import com.inspire.common.cookie.servlet.CookieUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/auth")
@Tag(name = "auth", description = "Authentication APIs")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieUtils cookieUtils;

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    public ResponseEntity<Void> signup(@RequestBody @Valid SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    @Operation(summary = "일반 로그인")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(request);

        cookieUtils.addCookie(response, "accessToken", loginResponse.getAccessToken(), null, "/", 3600, true);
        cookieUtils.addCookie(response, "refreshToken", loginResponse.getRefreshToken(), null, "/", 3600 * 24 * 14, true);

        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create("/login-success"))
                .body(loginResponse);
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

    @GetMapping("/error")
    public String error() {
        throw new AuthException(AuthErrorCode.TEST);
    }

    @GetMapping("/test2")
    public ResponseEntity<Void> test2(@Valid @ModelAttribute TestDTO testDTO) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/test3")
    public ResponseEntity<List<TestDTO>> test3(@Valid @ModelAttribute TestDTO testDTO) {
        return ResponseEntity.status(208).body(List.of(new TestDTO("hi"), new TestDTO("bye")));
    }
}
