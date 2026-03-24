package com.inspire.favorite.domain.dto;

import com.inspire.favorite.domain.entity.FavoriteEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRequestDTO {

    private Long userId;
    private String itemCode;

    
    public FavoriteEntity toEntity() {
        return FavoriteEntity.builder()
                .userId(this.userId)
                .itemCode(this.itemCode)
                .build();
    }
}