package com.inspire.favorite.dao;

import com.inspire.favorite.domain.entity.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {

    List<FavoriteEntity> findByUserId(Long userId);

    Optional<FavoriteEntity> findByUserIdAndItemCode(Long userId, String itemCode);

    void deleteByUserIdAndItemCode(Long userId, String itemCode);
}
