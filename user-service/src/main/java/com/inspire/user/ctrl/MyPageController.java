package com.inspire.user.ctrl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inspire.user.domain.dto.UserRequestDTO;
import com.inspire.user.domain.dto.UserResponseDTO;
import com.inspire.user.service.MyPageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/profile") 
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    // 1. 프로필 조회 (Read)
    @GetMapping("/read")
    public ResponseEntity<UserResponseDTO> read(@RequestHeader("X-User-Id") Long user) {
        System.out.println(">>>> User ctrl path : /read");
        System.out.println(">>>> params user : " + user);

        UserResponseDTO response = myPageService.read(user);
        
        if(response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK); // 200
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }
}