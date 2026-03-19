package com.inspire.auth.security.service;

import com.inspire.auth.infrastructure.store.RedisTokenStore;
import com.inspire.auth.security.oauth.OAuth2UserInfo;
import com.inspire.auth.security.oauth.OAuth2UserInfoFactory;
import com.inspire.auth.security.principal.InspireOAuth2User;
import com.inspire.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class InspireOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User delegateUser = delegate.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = delegateUser.getAttributes();

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.of(provider, attributes);
        log.debug("OAuth2UserInfo: {}", userInfo);

        OAuth2User oAuth2User = toOAuth2User(userInfo);
        log.debug("OAuth2User: {}", oAuth2User);
        return oAuth2User;
    }

    private OAuth2User toOAuth2User(OAuth2UserInfo oAuth2UserInfo) {
        return new InspireOAuth2User(
                oAuth2UserInfo.getExternalId(),
                oAuth2UserInfo.getName(),
                oAuth2UserInfo.getEmail(),
                oAuth2UserInfo.getProvider(),
                Set.of(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2UserInfo.getAttributes()
        );
    }
}
