package com.example.certificate_service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // 특정 자격증(item_code)의 모든 일정을 조회할 때 사용
    List<Schedule> findByCertificate_ItemCode(String itemCode);
    
    // 특정 시행년도의 일정 조회
    List<Schedule> findByImplYear(String implYear);
}