package com.example.certificate_service.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import com.example.certificate_service.domain.entity.ScheduleEntity;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    // 특정 자격증(item_code)의 모든 일정을 조회
    List<ScheduleEntity> findByCertificate_ItemCode(String itemCode);
    
    // 특정 시행년도의 일정 조회
    List<ScheduleEntity> findByImplYear(String implYear);

    // 접수 기간 내의 일정 조회
    @Query("SELECT s FROM ScheduleEntity s JOIN FETCH s.certificate c " +
           "WHERE :currentDate BETWEEN s.writtenRegStart AND s.writtenRegEnd")
    List<ScheduleEntity> findActiveSchedules(@Param("currentDate") LocalDate currentDate, Pageable pageable);

    // [기능 2] 다가오는 시험 (필기 원서접수 시작일이 현재 날짜보다 큰 일정 중 가장 가까운 3개)
    @Query("SELECT s FROM ScheduleEntity s JOIN FETCH s.certificate c " +
           "WHERE s.writtenRegStart > :currentDate " +
           "ORDER BY s.writtenRegStart ASC")
    List<ScheduleEntity> findUpcomingSchedules(@Param("currentDate") LocalDate currentDate, Pageable pageable);

    // [기능 3 수정] 특정 연도의 모든 시험 일정 조회 (캘린더용, N+1 방지)
    @Query("SELECT s FROM ScheduleEntity s JOIN FETCH s.certificate c WHERE s.implYear = :year")
    List<ScheduleEntity> findSchedulesByYear(@Param("year") String year);

    // [기능 4] 예정된 시험 개수 (원서접수 시작일이 현재보다 미래)
    @Query("SELECT COUNT(s) FROM ScheduleEntity s WHERE s.writtenRegStart > :currentDate")
    long countUpcomingSchedules(@Param("currentDate") LocalDate currentDate);

    // [기능 4] 접수 중인 시험 개수 (원서접수 시작일 <= 현재 <= 종료일)
    @Query("SELECT COUNT(s) FROM ScheduleEntity s WHERE :currentDate BETWEEN s.writtenRegStart AND s.writtenRegEnd")
    long countActiveSchedules(@Param("currentDate") LocalDate currentDate);

    // [기능 4] 접수 종료된 시험 개수 (원서접수 종료일이 현재보다 과거)
    @Query("SELECT COUNT(s) FROM ScheduleEntity s WHERE s.writtenRegEnd < :currentDate")
    long countCompletedSchedules(@Param("currentDate") LocalDate currentDate);

}