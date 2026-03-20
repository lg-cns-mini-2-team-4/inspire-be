package com.inspire.auth.infrastructure.client;

import com.inspire.auth.infrastructure.client.dto.UserProfileCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "http://localhost:8080")
public interface UserClient {

    @PostMapping("/users")
    void createUserProfile(@RequestBody UserProfileCreateRequest request);
}
