package com.inspire.auth.controller;

import com.inspire.auth.domain.dto.TestDTO;
import com.inspire.auth.domain.dto.response.AccessTokenDTO;
import com.inspire.auth.exception.AuthErrorCode;
import com.inspire.auth.exception.AuthException;
import com.inspire.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "auth", description = "임시")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "임시 요약")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(type = "string", example = "hi")))
    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/error")
    public String error() {
        throw new AuthException(AuthErrorCode.TEST);
    }

    @Operation(summary = "응응")
    @ApiResponse(responseCode = "204", description = "성공")
    @GetMapping("/test2")
    public ResponseEntity<Void> test2(@Valid @ModelAttribute TestDTO testDTO) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/test3")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = TestDTO.class)))
    public ResponseEntity<List<TestDTO>> test3(@Valid @ModelAttribute TestDTO testDTO) {
        return ResponseEntity.status(208).body(List.of(new TestDTO("hi"), new TestDTO("bye")));
    }

    @PostMapping("/reissue")
    public ResponseEntity<AccessTokenDTO> reissue(HttpServletResponse response, @CookieValue(name = "inspire_refresh") String refreshToken) {

        AccessTokenDTO accessTokenDTO = authService.reissue(response, refreshToken);
        return ResponseEntity.ok(accessTokenDTO);
    }
}
