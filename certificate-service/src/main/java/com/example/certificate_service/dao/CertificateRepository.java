package com.example.certificate_service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.certificate_service.domain.entity.CertificateEntity;

@Repository
public interface CertificateRepository extends JpaRepository<CertificateEntity, String> {
    // 종목 이름으로 검색이 필요한 경우를 대비해 추가
    java.util.Optional<CertificateEntity> findByItemName(String itemName);
}