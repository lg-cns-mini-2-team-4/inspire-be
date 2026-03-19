package com.inspire.auth.security.oauth;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

    @SuppressWarnings("unchecked")
    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super("naver", (Map<String, Object>) attributes.get("response"));
    }

    @Override
    public String getExternalId() {
        return (String) this.getAttributes().get("id");
    }

    @Override
    public String getEmail() {
        return (String) this.getAttributes().get("email");
    }

    @Override
    public String getName() {
        return (String) this.getAttributes().get("name");
    }
}
