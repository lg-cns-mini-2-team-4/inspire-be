package com.inspire.auth.controller;

import com.inspire.auth.domain.dto.request.UserLoginDTO;
import com.inspire.auth.domain.dto.response.AccessTokenDTO;
import com.inspire.auth.service.AuthService;
import com.inspire.common.jwt.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Profile("local")
@RestController
@RequestMapping("/v1/auth")
@Tag(name = "auth", description = "임시")
public class LocalAuthController implements AuthApiSpecification {

    private final String LOGIN_SUCCESS_URL;
    private final AuthService authService;
    private final JwtUtils jwtUtils;

    public LocalAuthController(@Value("${app.frontend.url.login-success}") String successUrl, AuthService authService, JwtUtils jwtUtils) {
        this.LOGIN_SUCCESS_URL = successUrl;
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register() {
        return null;
    }

    @PostMapping("/login")
    public ResponseEntity<AccessTokenDTO> login(HttpServletResponse res, @RequestBody UserLoginDTO userLoginDTO) {

        AccessTokenDTO accessTokenDTO = authService.login(res, userLoginDTO);
        return ResponseEntity.ok(accessTokenDTO);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "로그아웃", description = "서버 측 세션(Refresh Token)을 무효화하고 쿠키를 초기화합니다.")
    @ApiResponse(responseCode = "204", description = "로그아웃 성공")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse res,
                                       @RequestHeader(name = "Authorization", required = false) String authHeader,
                                       @CookieValue(name = "inspire_refresh", required = false) String refreshToken) {
        Long userId = null;
        log.debug("userId: {}", authHeader);
        log.debug("authHeader: {}", authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);
            userId = jwtUtils.getUserIdFromAccessToken(accessToken);
            log.debug("userId: {}", authHeader);
            log.debug("accessToken: {}", authHeader);
        }
        authService.logout(res, userId, refreshToken);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<AccessTokenDTO> reissue(HttpServletResponse response, @CookieValue(name = "inspire_refresh") String refreshToken) {

        AccessTokenDTO accessTokenDTO = authService.reissue(response, refreshToken);
        return ResponseEntity.ok(accessTokenDTO);
    }
}
