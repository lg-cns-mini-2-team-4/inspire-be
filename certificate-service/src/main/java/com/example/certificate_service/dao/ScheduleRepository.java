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
    // 특정 자격증(item_code)의 모든 일정을 조회할 때 사용
    List<ScheduleEntity> findByCertificate_ItemCode(String itemCode);
    
    // 특정 시행년도의 일정 조회
    List<ScheduleEntity> findByImplYear(String implYear);

    // [추가된 부분] 현재 날짜가 접수 기간 내에 있고, CertificateEntity와 Fetch Join하여 데이터 조회
    @Query("SELECT s FROM ScheduleEntity s JOIN FETCH s.certificate c " +
           "WHERE :currentDate BETWEEN s.writtenRegStart AND s.writtenRegEnd")
    List<ScheduleEntity> findActiveSchedules(@Param("currentDate") LocalDate currentDate, Pageable pageable);

}