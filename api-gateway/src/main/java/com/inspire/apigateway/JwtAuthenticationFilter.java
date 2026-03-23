package com.inspire.apigateway;

import com.inspire.common.jwt.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
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

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String bearerToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        log.debug("bearerToken: {}", bearerToken);

        String path = exchange.getRequest().getURI().getRawPath().trim();
        log.debug("path: {}", path);

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        try {

            String accessToken = bearerToken.substring(7);
            log.debug("accessToken: {}", accessToken);

            Claims claims = jwtUtils.parseAccessToken(accessToken);

            String id = claims.getSubject();
            List<String> roles = claims.get("roles", List.class);
            String rolesValue = String.join(", ", roles);
            log.debug("id: {}", id);
            log.debug("roles: {}", rolesValue);

            ServerWebExchange modifyExchange = exchange.mutate()
                    .request(builder -> builder
                            .header("X-User-Id", id)
                            .header("X-User-Roles", rolesValue)
                    ).build();

            return chain.filter(modifyExchange);

        } catch (Exception e) {
            e.printStackTrace();
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

    }
}
