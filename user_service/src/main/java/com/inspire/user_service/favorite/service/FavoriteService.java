package com.inspire.user_service.favorite.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inspire.user_service.favorite.dao.FavoriteRepository;
import com.inspire.user_service.favorite.domain.dto.FavoriteRequestDTO;
import com.inspire.user_service.favorite.domain.dto.FavoriteResponseDTO;
import com.inspire.user_service.favorite.domain.entity.FavoriteEntity;
import com.inspire.user_service.user.dao.UserRepository;
import com.inspire.user_service.user.domain.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;

    @Transactional
    public void add(Long userId, FavoriteRequestDTO request) {
        
        UserEntity user = userRepository.getReferenceById(userId);
        String itemCode = request.getItemCode();

        if (favoriteRepository.existsByUserAndItemCode(user, request.getItemCode())) {
            throw new RuntimeException("해당 항목이 이미 존재합니다. : " + itemCode);
        }

        FavoriteEntity favorite = request.toEntity(user);
        favoriteRepository.save(favorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponseDTO> list(Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found!!"));

        return favoriteRepository.findAllByUserOrderByCreateAtDesc(user)
                .stream()
                .map(FavoriteResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void remove(Long userId, FavoriteRequestDTO request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found!!"));

        String itemCode = request.getItemCode();

        if (!favoriteRepository.existsByUserAndItemCode(user, itemCode)) {
            throw new RuntimeException("삭제할 즐겨찾기 항목이 존재하지 않습니다: " + itemCode);
        }

        favoriteRepository.deleteByUserAndItemCode(user, itemCode);
    }
}
