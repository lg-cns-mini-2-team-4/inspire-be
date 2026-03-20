package com.inspire.user_service.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.inspire.user_service.user.domain.dto.UserRequestDTO;

// @FeignClient(name = "auth-service", url = "http://localhost:12321")
@FeignClient(name = "user-service")
public interface AuthServiceClient {

    @PostMapping("/profile/save") 
    void saveUserProfile(
        @RequestBody UserRequestDTO request, 
        @RequestHeader("X-User-Id") Long userId
    );
}
