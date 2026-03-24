package com.inspire.auth.config;

import com.inspire.auth.security.handler.OAuth2AuthenticationFailureHandler;
import com.inspire.auth.security.handler.OAuth2AuthenticationSuccessHandler;
import com.inspire.auth.security.oauth.RedisAuthorizationRequestRepository;
import com.inspire.auth.security.service.InspireOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.oauth2.client.jackson2.OAuth2ClientJackson2Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.jackson2.OAuth2ClientJackson2Module;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${app.frontend.url.base}")
    private String frontend;

    private final InspireOAuth2UserService oAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final RedisAuthorizationRequestRepository redisAuthorizationRequestRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//     @Bean
//     @Profile("!local")
//     public SecurityFilterChain filterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
//         http
//                 .sessionManagement(session -> session
//                         .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                 .cors(CorsConfigurer::disable)
//                 .csrf(CsrfConfigurer::disable)
//                 .authorizeHttpRequests(auth -> auth
//                         .anyRequest().permitAll())
//                 .httpBasic(HttpBasicConfigurer::disable)
//                 .formLogin(FormLoginConfigurer::disable)
//                 .oauth2Login(oauth -> oauth
//                         .authorizationEndpoint(auth -> auth
//                                 .baseUri("/oauth2/authorization")
//                                 .authorizationRequestRepository(redisAuthorizationRequestRepository)
//                         )
//                         .redirectionEndpoint(redirect -> redirect
//                                 .baseUri("/oauth2/code/*")
//                         )
//                         .userInfoEndpoint(userInfo -> userInfo
//                                 .userService(oAuth2UserService)
//                         )
//                         .successHandler(oAuth2AuthenticationSuccessHandler)
//                         .failureHandler(oAuth2AuthenticationFailureHandler)
//                 );
//         return http.build();
//     }


    @Bean
    @Profile("!local")
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. 세션 사용 안 함
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 2. CORS/CSRF 일단 끄기
            .cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            // 3. 모든 요청을 무조건 허용 (이게 핵심)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            // 4. 나머지 보안 기능들 명시적으로 끄기
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable);
            // .oauth2Login(...) 부분은 잠시 주석 처리하거나 지워보세요.

        return http.build();
    }

    @Bean
    @Profile("local")
    public SecurityFilterChain localFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(CorsConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .httpBasic(HttpBasicConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .oauth2Login(oauth -> oauth
                        .authorizationEndpoint(auth -> auth
                                .baseUri("/oauth2/authorization")
                                .authorizationRequestRepository(redisAuthorizationRequestRepository)
                        )
                        .redirectionEndpoint(redirect -> redirect
                                .baseUri("/oauth2/code/*")
                        )
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)
                        )
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler)
                );

        return http.build();
    }
}
