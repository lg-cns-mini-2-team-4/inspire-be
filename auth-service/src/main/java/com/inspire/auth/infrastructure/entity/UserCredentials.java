package com.inspire.auth.infrastructure.entity;

import com.inspire.auth.infrastructure.enums.OAuth2Provider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity
@Table(name = "user_credentials")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserCredentials extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    // email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "provider", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OAuth2Provider provider;

    @Column(name = "external_id")
    private String externalId;


    @Builder
    public UserCredentials(Long userId, String email, String passwordHash, OAuth2Provider provider, String externalId) {
        Assert.notNull(provider, "provider must not be null.");
        Assert.isTrue(
                (provider == OAuth2Provider.INSPIRE && externalId == null) ||
                        (provider != OAuth2Provider.INSPIRE && externalId != null),
                "externalId must be null for INSPIRE, and non-null otherwise."
        );
        this.userId = userId;
        this.email = email;
        this.provider = provider;
        this.passwordHash = passwordHash;
        this.externalId = externalId;

    }
}
