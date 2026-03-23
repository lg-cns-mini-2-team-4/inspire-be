package com.example.certificate_service.dao;
import com.example.certificate_service.domain.entity.CertificateEntity;
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
    // 특정 자격증(item_code)의 모든 일정을 조회할 때 사용
    List<ScheduleEntity> findByCertificate_ItemCode(String itemCode);
    
    // 특정 시행년도의 일정 조회
    List<ScheduleEntity> findByImplYear(String implYear);

    // 1. 현재 접수 중인 시험 조회
    // [기능 3] 현재 날짜가 접수 기간 내에 있는 모든 시험 반환
    @Query("SELECT s FROM ScheduleEntity s JOIN FETCH s.certificate c " +
           "WHERE (:currentDate BETWEEN s.writtenRegStart AND s.writtenRegEnd) " +      // 필기 접수 중
           "   OR (:currentDate BETWEEN s.practicalRegStart AND s.practicalRegEnd)")    // 실기 접수 중
    List<ScheduleEntity> findActiveSchedules(@Param("currentDate") LocalDate currentDate, Pageable pageable);

    // 2. 다가오는 시험 조회
    @Query("SELECT s FROM ScheduleEntity s JOIN FETCH s.certificate c " +
           "WHERE (YEAR(s.writtenRegStart) = YEAR(:currentDate) OR YEAR(s.practicalRegStart) = YEAR(:currentDate)) " + // 올해 일정만 필터링
           "AND ((s.writtenRegStart > :currentDate) OR (s.practicalRegStart > :currentDate)) " +
           "ORDER BY LEAST(COALESCE(s.writtenRegStart, s.practicalRegStart), COALESCE(s.practicalRegStart, s.writtenRegStart)) ASC")
    List<ScheduleEntity> findUpcomingSchedules(@Param("currentDate") LocalDate currentDate, Pageable pageable);

    // // 3-1. 대시보드 상태별 시험 개수 조회 : 접수 예정
    // @Query("SELECT COUNT(s) FROM ScheduleEntity s " +
    //        "WHERE (YEAR(s.writtenRegStart) = YEAR(:currentDate) OR YEAR(s.practicalRegStart) = YEAR(:currentDate)) " +
    //        "AND (s.writtenRegStart > :currentDate OR s.practicalRegStart > :currentDate)" +
    //        "ORDER BY LEAST(COALESCE(s.writtenRegStart, s.practicalRegStart), COALESCE(s.practicalRegStart, s.writtenRegStart)) ASC")
    // long countUpcomingSchedules(@Param("currentDate") LocalDate currentDate);

    // // 3-2. 대시보드 상태별 시험 개수 조회 : 접수 중
    // @Query("SELECT COUNT(s) FROM ScheduleEntity s " +
    //     "WHERE (YEAR(s.writtenRegStart) = YEAR(:currentDate) OR YEAR(s.practicalRegStart) = YEAR(:currentDate)) " +
    //     "AND (" +
    //     "  (:currentDate BETWEEN s.writtenRegStart AND s.writtenRegEnd) " + // 필기 접수 중
    //     "  OR " +
    //     "  (:currentDate BETWEEN s.practicalRegStart AND s.practicalRegEnd)" + // 실기 접수 중
    //     ")")
    // long countActiveSchedules(@Param("currentDate") LocalDate currentDate);

    // // 3-3. 대시보드 상태별 시험 개수 조회 : 접수 종료
    // @Query("SELECT COUNT(s) FROM ScheduleEntity s " +
    //     "WHERE (YEAR(s.writtenRegEnd) = YEAR(:currentDate) OR YEAR(s.practicalRegEnd) = YEAR(:currentDate)) " + 
    //     "AND (" +
    //     "  (s.writtenRegEnd < :currentDate) " + // 필기 접수 종료
    //     "  OR " +
    //     "  (s.practicalRegEnd < :currentDate)" + // 실기 접수 종료
    //     ")")
    // long countCompletedSchedules(@Param("currentDate") LocalDate currentDate);
// 3-1. 대시보드 : 접수 예정
    // 필기 접수조차 시작 안 했거나 (아예 시작 전), 필기는 끝났고 실기 접수를 기다리는 중인 경우
    @Query("SELECT COUNT(s) FROM ScheduleEntity s " +
           "WHERE s.writtenRegStart > :currentDate " +
           "   OR (s.writtenRegEnd < :currentDate AND s.practicalRegStart > :currentDate)")
    long countUpcomingSchedules(@Param("currentDate") LocalDate currentDate);

    // 3-2. 대시보드 : 접수 중
    // 현재 날짜가 [필기 접수 기간] 또는 [실기 접수 기간]에 정확히 포함되는 경우
    @Query("SELECT COUNT(s) FROM ScheduleEntity s " +
           "WHERE (:currentDate BETWEEN s.writtenRegStart AND s.writtenRegEnd) " +
           "   OR (:currentDate BETWEEN s.practicalRegStart AND s.practicalRegEnd)")
    long countActiveSchedules(@Param("currentDate") LocalDate currentDate);

    // 3-3. 대시보드 : 접수 종료
    // 가장 마지막 일정인 '실기 접수'까지 완전히 지나간 경우
    @Query("SELECT COUNT(s) FROM ScheduleEntity s " +
           "WHERE s.practicalRegEnd < :currentDate")
    long countCompletedSchedules(@Param("currentDate") LocalDate currentDate);



    
    // 중복 저장 방지용
    boolean existsByImplYearAndImplSeqAndCertificate(String implYear, Integer implSeq, CertificateEntity certificate);



    // [기능 4] 전체 시험 조회 (올해 ~ 내년)
    @Query("SELECT s FROM ScheduleEntity s JOIN FETCH s.certificate c " +
              "WHERE s.implYear IN (:thisYear, :nextYear) " +
              "ORDER BY s.writtenRegStart ASC")
    List<ScheduleEntity> findSchedulesByYears(@Param("thisYear") String thisYear, @Param("nextYear") String nextYear);

    // [기능 5] 특정 연도의 모든 시험 일정 조회 (캘린더용, N+1 방지)
    @Query("SELECT s FROM ScheduleEntity s JOIN FETCH s.certificate c WHERE s.implYear = :year")
    List<ScheduleEntity> findSchedulesByYear(@Param("year") String year);















}