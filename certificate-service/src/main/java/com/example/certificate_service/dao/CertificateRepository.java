package com.example.certificate_service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, String> {
    // 종목 이름으로 검색이 필요한 경우를 대비해 추가
    java.util.Optional<Certificate> findByItemName(String itemName);
}