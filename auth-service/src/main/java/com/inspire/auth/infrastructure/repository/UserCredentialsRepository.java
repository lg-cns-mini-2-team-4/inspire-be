package com.inspire.auth.infrastructure.repository;

import com.inspire.auth.infrastructure.entity.UserCredentials;
import com.inspire.auth.infrastructure.enums.OAuth2Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {
    Optional<UserCredentials> findByEmail(String email);
    Optional<UserCredentials> findByProviderAndExternalId(OAuth2Provider OAuth2Provider, String externalId);

    boolean existsByEmail(String email);
}
