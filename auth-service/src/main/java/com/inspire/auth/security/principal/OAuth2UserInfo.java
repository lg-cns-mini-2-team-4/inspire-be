package com.inspire.auth.security.principal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public abstract class OAuth2UserInfo {
    protected final String provider;
    protected final Map<String, Object> attributes;

    public abstract String getExternalId();

    public abstract String getEmail();

    public abstract String getName();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Provider: [");
        sb.append(this.getProvider());
        sb.append("], External Id: [");
        sb.append(this.getExternalId());
        sb.append("], Name: [");
        sb.append(this.getName());
        sb.append("], Email: [");
        sb.append(this.getEmail());
        sb.append("] User Attributes: [");
        sb.append(this.getAttributes());
        sb.append("]");
        return sb.toString();
    }
}
