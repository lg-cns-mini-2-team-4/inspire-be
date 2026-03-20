package com.inspire.auth.controller;

import com.inspire.auth.domain.dto.response.AccessTokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@Tag(name = "auth", description = "임시")
public interface AuthApiSpecification {

    @Operation(summary = "회원가입")
    @ApiResponses({
            @ApiResponse(responseCode = "200")
    })
    ResponseEntity<Void> register();

    @Operation(summary = "사용자 로그인", description = "아이디와 비밀번호로 로그인합니다. 성공 시 쿠키에 Refresh Token이 설정됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 실패 (아이디/비밀번호 불일치)"),
    })
    ResponseEntity<AccessTokenDTO> login(HttpServletResponse res,
                               @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                       description = "로그인 정보",
                                       required = true,
                                       content = @Content(schema = @Schema(implementation = UserLoginDTO.class))
                               )
                               @RequestBody UserLoginDTO userLoginDTO) throws URISyntaxException;

    @Operation(summary = "토큰 재발급", description = "쿠키의 Refresh Token을 확인하여 새로운 Access Token을 발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재발급 성공"),
            @ApiResponse(responseCode = "401", description = "유효하지 않거나 만료된 Refresh Token")
    })
    ResponseEntity<AccessTokenDTO> reissue(HttpServletResponse response,
                                           @Parameter(in = ParameterIn.COOKIE, description = "Refresh Token")
                                           @CookieValue(name = "inspire_refresh") String refreshToken);
}
