package com.inspire.auth.controller;

import com.inspire.auth.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TempController {

    private final RefreshTokenService refreshTokenService;

    @GetMapping("/refresh/{id}")
    public ResponseEntity<Void> refresh(HttpServletResponse response, @PathVariable(name = "id") Long id) {
        refreshTokenService.saveRefreshTokenAndAddCookie(response, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/check")
    public ResponseEntity<String> issue(@CookieValue(name = "inspire_refresh") String cookie) {
        return ResponseEntity.ok(cookie);
    }
}
