package com.inspire.user_service.favorite.domain.dto;

import java.time.LocalDateTime;

import com.inspire.user_service.favorite.domain.entity.FavoriteEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteResponseDTO {

    private Long favoriteId;   
    private String itemCode;   
    private LocalDateTime createdAt; 

    public static FavoriteResponseDTO fromEntity(FavoriteEntity entity) {
        return FavoriteResponseDTO.builder()
                .favoriteId(entity.getFavoriteId())
                .itemCode(entity.getItemCode())
                .createdAt(entity.getCreateAt())
                .build();
    }

}
