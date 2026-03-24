package com.inspire.certificate_service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inspire.certificate_service.domain.entity.CertificateEntity;

@Repository
public interface CertificateRepository extends JpaRepository<CertificateEntity, String> {

}