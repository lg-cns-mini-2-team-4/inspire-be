package com.inspire.auth.security.oauth;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    private final String id;
    private final String name;

    @SuppressWarnings("unchecked")
    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super("kakao", (Map<String, Object>) attributes.get("kakao_account"));
        this.id = attributes.get("id").toString();
        Map<String, Object> profile = (Map<String, Object>) this.getAttributes().get("profile");
        this.name = profile.get("nickname").toString();
    }

    @Override
    public String getExternalId() {
        return this.id;
    }

    @Override
    public String getEmail() {
        return (String) this.getAttributes().get("email");
    }

    @Override
    public String getName() {
        return this.name;
    }
}
