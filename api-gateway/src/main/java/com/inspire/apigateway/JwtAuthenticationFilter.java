package com.inspire.apigateway;

import com.inspire.common.jwt.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public int getOrder() {
        return -1;
    }

    // white list 관리 안함
    // 그냥 토큰 있으면 X-user-id로 바꿔줌
    @Override
    @SuppressWarnings("unchecked")
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getRawPath().trim();
        log.info("path: {}", path);

        // 1. 토큰 검사를 건너뛸 경로 추가
        if (path.contains("/auth/signup") || path.contains("/auth/login") || path.contains("/v3/api-docs")) {
            return chain.filter(exchange); // 그냥 통과!
        }
        
        String bearerToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        log.info("bearerToken: {}", bearerToken);
        

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        try {

            String accessToken = bearerToken.substring(7);
            log.info("accessToken: {}", accessToken);

            Claims claims = jwtUtils.parseAccessToken(accessToken);

            String id = claims.getSubject();
            List<String> roles = claims.get("roles", List.class);
            String rolesValue = String.join(", ", roles);
            log.info("id: {}", id);
            log.info("roles: {}", rolesValue);

            ServerWebExchange modifyExchange = exchange.mutate()
                    .request(builder -> builder
                            .headers(headers -> {
                                headers.remove("Authorization");
                                headers.add("X-User-Id", id);
                                headers.add("X-User-Roles", rolesValue);
                            })
                    ).build();

            return chain.filter(modifyExchange);

        } catch (Exception e) {
            e.printStackTrace();
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

    }
}
