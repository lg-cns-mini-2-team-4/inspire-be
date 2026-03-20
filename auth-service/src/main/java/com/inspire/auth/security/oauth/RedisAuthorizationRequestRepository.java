package com.inspire.auth.security.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private final RedisTemplate<String, OAuth2AuthorizationRequest> redisTemplate;
    private static final String PREFIX = "oauth2_auth_request";

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return redisTemplate.opsForValue().get(addPrefix(request.getParameter("state")));
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        redisTemplate.opsForValue().set(addPrefix(authorizationRequest.getState()), authorizationRequest, Duration.ofMinutes(5));
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return redisTemplate.opsForValue().getAndDelete(addPrefix(request.getParameter("state")));
    }

    private String addPrefix(String state) {
        return PREFIX + ":" + state;
    }
}
