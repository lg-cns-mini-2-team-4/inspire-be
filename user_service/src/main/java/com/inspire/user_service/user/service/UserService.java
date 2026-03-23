package com.inspire.user_service.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inspire.user_service.user.dao.UserRepository;
import com.inspire.user_service.user.domain.dto.UserRequestDTO;
import com.inspire.user_service.user.domain.dto.UserResponseDTO;
import com.inspire.user_service.user.domain.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponseDTO save(UserRequestDTO request, Long userId) {
        System.out.println(">>> User service create");

        if (userRepository.existsById(userId)) {
            System.out.println(">>> 중복 유저 발생: " + userId);
            return null;
        }

        UserEntity userEntity = userRepository.save(request.toEntity(userId));
        return UserResponseDTO.fromEntity(userEntity);
    }
    

    @Transactional(readOnly = true) 
    public UserResponseDTO read(Long userId) {
        System.out.println(">>> User service read for userId: " + userId);

        // 1. DB에서 userId로 유저 찾기
        return userRepository.findById(userId)
                .map(UserResponseDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없음: " + userId));
    }
}