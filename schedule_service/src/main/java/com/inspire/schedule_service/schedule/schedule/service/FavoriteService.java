package com.inspire.schedule_service.schedule.schedule.service;

import org.springframework.transaction.annotation.Transactional;

import com.inspire.schedule_service.schedule.schedule.domain.dto.FavoriteRequestDTO;

public class FavoriteService {
    @Transactional
    public void addFavoriteWithSchedules(FavoriteRequestDTO request, Long userId) {
        // 1. Favorite 테이블에 자격증 카드 정보 저장 (1줄)
        FavoriteEntity favorite = FavoriteEntity.builder()
                .userId(userId)
                .itemCode(request.getItemCode())
                .itemName(request.getItemName())
                .officeName(request.getOfficeName())
                .build();
        favoriteRepository.save(favorite);

        // 2. Schedule 테이블에 일정 리스트 저장 (N줄)
        // 이미 만들어둔 scheduleService.create(List<ScheduleRequestDTO>, userId)를 호출!
        scheduleService.create(request.getSchedules(), userId);
    }
}
