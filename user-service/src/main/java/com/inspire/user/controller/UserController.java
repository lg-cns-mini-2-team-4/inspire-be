package com.inspire.user.controller;

import com.inspire.user.domain.dto.request.UserCreateRequest;
import com.inspire.user.domain.dto.request.UserUpdateRequest;
import com.inspire.user.domain.dto.response.UserResponse;
import com.inspire.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "user", description = "User APIs")
public class UserController {

    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<Void> createUser(@RequestBody UserCreateRequest request) {
        userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUser(@RequestHeader("X-User-Id") Long id) {
        UserResponse user = userService.getUser(id);

        return ResponseEntity.ok(user);
    }

    @PatchMapping("/me")
    public ResponseEntity<Void> updateUser(@RequestHeader("X-User-Id") Long id, UserUpdateRequest request) {
        userService.updateUser(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(@RequestHeader("X-User-Id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
