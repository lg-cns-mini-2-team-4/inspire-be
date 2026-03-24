package com.inspire.certificate_service.dao;

import com.inspire.certificate_service.domain.entity.CertificateEntity;
import com.inspire.certificate_service.domain.entity.ExamEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExamRepository extends JpaRepository<ExamEntity, Long>, JpaSpecificationExecutor<ExamEntity> {

    List<ExamEntity> findByCertificate_ItemCode(String itemCode);

    // 1. 접수 중인 시험
    @Query("SELECT e FROM ExamEntity e WHERE e.type = 'WR' " +
           "AND :today BETWEEN e.startDate AND e.endDate")
    List<ExamEntity> findActiveWrittenExams(@Param("today") LocalDate today, Pageable pageable);

    // 2. 접수 예정인 시험
    @Query("SELECT e FROM ExamEntity e WHERE e.type = 'WR' " +
           "AND e.startDate > :today ORDER BY e.startDate ASC")
    List<ExamEntity> findUpcomingWrittenExams(@Param("today") LocalDate today, Pageable pageable);

    // 개수 카운트
    @Query("SELECT COUNT(e) FROM ExamEntity e WHERE e.type = 'WR' AND :today BETWEEN e.startDate AND e.endDate")
    long countActiveWritten(@Param("today") LocalDate today);

    // 개수 카운트
    @Query("SELECT COUNT(e) FROM ExamEntity e WHERE e.type = 'WR' AND e.startDate > :today")
    long countUpcomingWritten(@Param("today") LocalDate today);

    List<ExamEntity> findByCertificate_ItemCodeAndImplYearAndImplSeq(String itemCode, String implYear, Integer implSeq);

    boolean existsByCertificateAndImplYearAndImplSeqAndType(CertificateEntity certificate,
                                                            String implYear,
                                                            Integer implSeq,
                                                            String type
    );

}