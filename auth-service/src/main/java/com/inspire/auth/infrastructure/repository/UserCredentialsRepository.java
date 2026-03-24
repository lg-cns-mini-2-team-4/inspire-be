package com.inspire.auth.infrastructure.repository;

import com.inspire.auth.infrastructure.entity.UserCredentials;
import com.inspire.auth.infrastructure.enums.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {
    Optional<UserCredentials> findByEmailAndProvider(String email, Provider provider);
    boolean existsByEmailAndProvider(String loginId, Provider provider);
    Optional<UserCredentials> findByProviderAndExternalId(Provider Provider, String externalId);
}
