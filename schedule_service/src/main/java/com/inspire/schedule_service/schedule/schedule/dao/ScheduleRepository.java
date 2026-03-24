package com.inspire.schedule_service.schedule.schedule.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.inspire.schedule_service.schedule.schedule.domain.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long>, JpaSpecificationExecutor<ScheduleEntity> {
    // 유저의 전체 일정 조회 (GET schedules/me)
    List<ScheduleEntity> findAllByUserId(Long userId);

    // 특정 유저의 자격증 관련 일정만 조회 (ref_id 추출용)
    List<ScheduleEntity> findAllByUserIdAndType(Long userId, String type);

    // 단건 상세 조회 (내 일정인지 보안 검증 포함)
    Optional<ScheduleEntity> findByIdAndUserId(Long id, Long userId);

    // 즐겨찾기 해제 시 일괄 삭제
    void deleteByUserIdAndRefId(Long userId, String refId);
}