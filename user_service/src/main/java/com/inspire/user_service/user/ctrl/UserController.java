package com.inspire.user_service.user.ctrl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inspire.user_service.user.domain.dto.UserRequestDTO;
import com.inspire.user_service.user.domain.dto.UserResponseDTO;
import com.inspire.user_service.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/profile") 
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/save")
    public ResponseEntity<UserResponseDTO> save( @RequestBody UserRequestDTO request,
                                                @RequestHeader("X-User-Id") Long userId) {
        
        System.out.println(">>>> User ctrl path : /save");
        System.out.println(">>>> Header User-Id : " + userId);

        UserResponseDTO response = userService.save(request, userId);
        
        if (response != null) {
            return new ResponseEntity<>(HttpStatus.CREATED); 
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }
    }

    // 1. 프로필 조회 (Read)
    @GetMapping("/read")
    public ResponseEntity<UserResponseDTO> read(@RequestHeader("X-User-Id") Long userId) {
        System.out.println(">>>> User ctrl path : /read");
        System.out.println(">>>> params user : " + userId);

        UserResponseDTO response = userService.read(userId);
        
        if(response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK); // 200
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }
}