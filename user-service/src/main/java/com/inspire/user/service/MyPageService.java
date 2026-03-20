package com.inspire.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inspire.user.client.AuthServiceClient; 
import com.inspire.user.dao.UserRepository;
import com.inspire.user.domain.dto.UserResponseDTO;
import com.inspire.user.domain.entity.UserEntity;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPageService {

    private final UserRepository userRepository;
    private final AuthServiceClient authServiceClient;

    @Transactional(readOnly = true)
    public UserResponseDTO read(Long user) {
        System.out.println(">>>> User service read");
        
        UserEntity userEntity = userRepository.findById(user)
                .orElseThrow(() -> new EntityNotFoundException("유저 정보를 찾을 수 없습니다"));

        String email = "";
        try {
            email = authServiceClient.getUserEmail(user); 
            System.out.println(">>>> Auth-service에서 받아온 이메일: " + email);
        } catch (Exception e) {
            System.out.println(">>>> Auth-service 통신 실패: " + e.getMessage());
            email = "이메일 정보 없음"; 
        }

        return UserResponseDTO.fromEntity(userEntity, email);
    }
}