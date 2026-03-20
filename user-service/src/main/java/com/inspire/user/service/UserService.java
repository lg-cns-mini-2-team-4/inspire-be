package com.inspire.user.service;

import com.inspire.user.controller.dto.UserProfileCreateRequest;
import com.inspire.user.domain.entity.User;
import com.inspire.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void createUserProfile(UserProfileCreateRequest request) {
        User user = User.builder()
                .id(request.getUserId())
                .name(request.getName())
                .email(request.getEmail())
                .build();
        userRepository.save(user);
    }
}
