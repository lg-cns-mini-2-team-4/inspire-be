package com.inspire.user.controller;

import com.inspire.user.domain.dto.request.UserCreateRequest;
import com.inspire.user.domain.dto.request.UserUpdateRequest;
import com.inspire.user.domain.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface UserApiSpecification {
    @Operation(summary = "유저 생성", security = {})
    ResponseEntity<Void> createUser(@RequestBody UserCreateRequest request);

    @Operation(summary = "유저 조회")
    ResponseEntity<UserResponse> getUser(@RequestHeader("X-User-Id") Long id);

    @Operation(summary = "유저 수정")
    ResponseEntity<Void> updateUser(@RequestHeader("X-User-Id") Long id, UserUpdateRequest request);

    @Operation(summary = "유저 삭제")
    ResponseEntity<Void> deleteUser(@RequestHeader("X-User-Id") Long id);
}
