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
    // [기능 1] 현재 날짜가 접수 기간 내에 있고, CertificateEntity와 Fetch Join하여 데이터 조회
    // [기능 3] 현재 날짜가 접수 기간 내에 있는 모든 시험 반환
    @Query("SELECT s FROM ScheduleEntity s JOIN FETCH s.certificate c " +
           "WHERE (:currentDate BETWEEN s.writtenRegStart AND s.writtenRegEnd) " +
           "   OR (:currentDate BETWEEN s.practicalRegStart AND s.practicalRegEnd)")
    List<ScheduleEntity> findActiveSchedules(@Param("currentDate") LocalDate currentDate, Pageable pageable);

    // [기능 2] 다가오는 시험 (필기 원서접수 시작일이 현재 날짜보다 큰 일정 중 가장 가까운 3개)
       @Query("SELECT s FROM ScheduleEntity s JOIN FETCH s.certificate c " +
              "WHERE (YEAR(s.writtenRegStart) = YEAR(:currentDate) OR YEAR(s.practicalRegStart) = YEAR(:currentDate)) " + // 올해 일정만 필터링
              "AND ((s.writtenRegStart > :currentDate) OR (s.practicalRegStart > :currentDate)) " +
              "ORDER BY LEAST(COALESCE(s.writtenRegStart, s.practicalRegStart), COALESCE(s.practicalRegStart, s.writtenRegStart)) ASC")
       List<ScheduleEntity> findUpcomingSchedules(@Param("currentDate") LocalDate currentDate, Pageable pageable);







    // [기능 4] 전체 시험 조회 (올해 ~ 내년)
    @Query("SELECT s FROM ScheduleEntity s JOIN FETCH s.certificate c " +
              "WHERE s.implYear IN (:thisYear, :nextYear) " +
              "ORDER BY s.writtenRegStart ASC")
    List<ScheduleEntity> findSchedulesByYears(@Param("thisYear") String thisYear, @Param("nextYear") String nextYear);

    // [기능 5] 특정 연도의 모든 시험 일정 조회 (캘린더용, N+1 방지)
    @Query("SELECT s FROM ScheduleEntity s JOIN FETCH s.certificate c WHERE s.implYear = :year")
    List<ScheduleEntity> findSchedulesByYear(@Param("year") String year);






    // [기능 4] 예정된 시험 개수 (원서접수 시작일이 현재보다 미래)
        @Query("SELECT COUNT(s) FROM ScheduleEntity s " +
            "WHERE (YEAR(s.writtenRegStart) = YEAR(:currentDate) OR YEAR(s.practicalRegStart) = YEAR(:currentDate)) " +
            "AND (s.writtenRegStart > :currentDate OR s.practicalRegStart > :currentDate)")
       long countUpcomingSchedules(@Param("currentDate") LocalDate currentDate);

    // [기능 4] 접수 중인 시험 개수 (원서접수 시작일 <= 현재 <= 종료일)
    @Query("SELECT COUNT(s) FROM ScheduleEntity s " +
        "WHERE (YEAR(s.writtenRegStart) = YEAR(:currentDate) OR YEAR(s.practicalRegStart) = YEAR(:currentDate)) " +
        "AND (" +
        "  (:currentDate BETWEEN s.writtenRegStart AND s.writtenRegEnd) " + // 필기 접수 중
        "  OR " +
        "  (:currentDate BETWEEN s.practicalRegStart AND s.practicalRegEnd)" + // 실기 접수 중
        ")")
    long countActiveSchedules(@Param("currentDate") LocalDate currentDate);

    // [기능 5] 접수 종료된 시험 개수 (원서접수 종료일이 현재보다 과거)
    @Query("SELECT COUNT(s) FROM ScheduleEntity s " +
        "WHERE (YEAR(s.writtenRegEnd) = YEAR(:currentDate) OR YEAR(s.practicalRegEnd) = YEAR(:currentDate)) " + 
        "AND (" +
        "  (s.writtenRegEnd < :currentDate) " + // 필기 접수 종료
        "  OR " +
        "  (s.practicalRegEnd < :currentDate)" + // 실기 접수 종료
        ")")
    long countCompletedSchedules(@Param("currentDate") LocalDate currentDate);
}