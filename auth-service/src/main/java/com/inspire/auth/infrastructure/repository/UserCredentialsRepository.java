package com.inspire.auth.infrastructure.repository;

import com.inspire.auth.infrastructure.entity.UserCredentials;
import com.inspire.auth.infrastructure.enums.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {
    Optional<UserCredentials> findByLoginIdAndProvider(String loginId, Provider provider);
    boolean existsByLoginIdAndProvider(String loginId, Provider provider);
}
