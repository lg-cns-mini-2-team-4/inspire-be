package com.inspire.user_service.favorite.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.inspire.user_service.favorite.domain.entity.FavoriteEntity;
import com.inspire.user_service.user.domain.entity.UserEntity;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
    @Transactional
    void deleteByUserAndItemCode(UserEntity user, String itemCode);

    boolean existsByUserAndItemCode(UserEntity user, String itemCode);

    List<FavoriteEntity> findAllByUserOrderByCreateAtDesc(UserEntity user);
    
}
