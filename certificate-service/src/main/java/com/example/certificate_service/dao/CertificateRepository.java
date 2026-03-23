package com.example.certificate_service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.certificate_service.domain.entity.CertificateEntity;

@Repository
public interface CertificateRepository extends JpaRepository<CertificateEntity, String> {

}