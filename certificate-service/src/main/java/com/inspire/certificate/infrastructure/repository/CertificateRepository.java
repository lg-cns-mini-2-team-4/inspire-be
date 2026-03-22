package com.inspire.certificate.infrastructure.repository;

import com.inspire.certificate.infrastructure.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateRepository extends JpaRepository<Certificate, String> {
}
