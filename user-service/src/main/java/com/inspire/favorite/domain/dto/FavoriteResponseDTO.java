package com.inspire.favorite.domain.dto;

import com.inspire.favorite.domain.entity.FavoriteEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteResponseDTO {

    private Long interestId;
    private Long userId;
    private String itemCode;
    private LocalDateTime createdAt;

    
    public static FavoriteResponseDTO fromEntity(FavoriteEntity entity) {
        return FavoriteResponseDTO.builder()
                .interestId(entity.getInterestId())
                .userId(entity.getUserId())
                .itemCode(entity.getItemCode())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}