package com.inspire.schedule_service.schedule.schedule.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inspire.schedule_service.schedule.schedule.domain.entity.ScheduleEntity;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    // 특정 사용자의 전체 일정 조회
    List<ScheduleEntity> findAllByUser(Long user);

    // 특정 사용자의 특정 일정 조회 (보안 및 권한 확인용)
    Optional<ScheduleEntity> findByIdAndUser(Long id, Long user);
}
