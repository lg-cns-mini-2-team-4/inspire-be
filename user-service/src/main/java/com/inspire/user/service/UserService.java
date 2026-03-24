package com.inspire.user.service;

import com.inspire.common.core.exception.ErrorCode;
import com.inspire.user.domain.dto.request.UserCreateRequest;
import com.inspire.user.domain.dto.request.UserUpdateRequest;
import com.inspire.user.domain.dto.response.UserResponse;
import com.inspire.user.exception.UserErrorCode;
import com.inspire.user.exception.UserException;
import com.inspire.user.infrastructure.entity.UserEntity;
import com.inspire.user.infrastructure.repository.UserRepository;
import com.inspire.user.mapper.UserEntityMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponse getUser(Long id) {
        return userRepository.findById(id)
                .map(UserEntityMapper::toUserResponse)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void createUser(UserCreateRequest request) {

        if (userRepository.existsById(request.getId())) {
            throw new UserException(UserErrorCode.USER_ALREADY_EXISTS);
        }

        UserEntity userEntity = UserEntityMapper.fromUserCreate(request);
        userRepository.save(userEntity);
    }

    @Transactional
    public void deleteUser(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        userRepository.delete(user);
    }


    @Transactional
    public void updateUser(Long id, UserUpdateRequest request) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        request.getName().ifPresent(userEntity::updateName);
        request.getPhone().ifPresent(userEntity::updatePhone);
        request.getEmail().ifPresent(userEntity::updateEmail);
    }
}
