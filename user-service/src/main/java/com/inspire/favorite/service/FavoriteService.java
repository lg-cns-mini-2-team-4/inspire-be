package com.inspire.favorite.service;

import com.inspire.favorite.domain.dto.*;
import com.inspire.favorite.domain.entity.FavoriteEntity;
import com.inspire.favorite.dao.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    //  즐겨찾기 추가
    public FavoriteResponseDTO addFavorite(FavoriteRequestDTO requestDTO) {

        favoriteRepository.findByUserIdAndItemCode(
                requestDTO.getUserId(),
                requestDTO.getItemCode()
        ).ifPresent(f -> {
            throw new RuntimeException("이미 즐겨찾기 존재");
        });

        
        FavoriteEntity favorite = requestDTO.toEntity();

        FavoriteEntity saved = favoriteRepository.save(favorite);

         
        return FavoriteResponseDTO.fromEntity(saved);
    }

    //  목록 조회
    public List<FavoriteResponseDTO> getFavorites(Long userId) {
        return favoriteRepository.findByUserId(userId)
                .stream()
                .map(FavoriteResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    //  삭제
    public void removeFavorite(Long userId, String itemCode) {
        favoriteRepository.deleteByUserIdAndItemCode(userId, itemCode);
    }
}