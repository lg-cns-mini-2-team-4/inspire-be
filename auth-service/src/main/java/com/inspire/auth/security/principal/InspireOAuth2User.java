package com.inspire.auth.security.principal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Getter
@ToString
public class InspireOAuth2User implements OAuth2User {
    private final String externalId;
    private final String externalName;
    private final String email;
    private final String provider;
    private final Set<GrantedAuthority> authorities;
    private final Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return this.externalId;
    }
}
