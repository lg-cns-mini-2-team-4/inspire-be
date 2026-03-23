package com.inspire.user_service.favorite.domain.dto;

import com.inspire.user_service.favorite.domain.entity.FavoriteEntity;
import com.inspire.user_service.user.domain.entity.UserEntity;

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
public class FavoriteRequestDTO {

    private String itemCode;

    public FavoriteEntity toEntity(UserEntity user) {
        return FavoriteEntity.builder()
                .user(user)
                .itemCode(this.itemCode)
                .build();
    }

}
