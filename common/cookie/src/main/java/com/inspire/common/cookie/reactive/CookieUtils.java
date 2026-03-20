package com.inspire.common.cookie.reactive;

import java.util.Optional;

import com.inspire.common.cookie.model.CookieAttributes;
import com.inspire.common.cookie.config.CookieProperties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.web.server.ServerWebExchange;

/**
 * Utility class for managing HTTP cookies in reactive-based applications.
 *
 * <p>
 * Provides convenience methods for retrieving, adding, and deleting cookies
 * using {@link ServerWebExchange}.
 * Supports both direct cookie attributes and the {@link CookieAttributes}.
 *
 * <hr>
 *
 * 리액티브 기반 애플리케이션에서 HTTP 쿠키를 관리하기 위한 유틸리티 클래스.
 *
 * <p>
 * {@link ServerWebExchange} 기반으로,
 * 쿠키의 조회, 추가, 삭제를 위한 기능을 제공하는 클래스입니다.
 *
 * <hr>
 *
 * @author Wooseong Urm
 * @see ServerWebExchange
 * @see CookieAttributes
 * @since 1.0.0
 */
@Slf4j
@Getter
public class CookieUtils {

    private final String defaultDomain;
    /**
     * Default Secure flag. <hr>
     * <p>
     * 기본 Secure 플래그.
     */
    private final Boolean defaultSecure;
    /**
     * Default SameSite attribute value. <hr>
     * <p>
     * 기본 SameSite 속성값.
     */
    private final String defaultSameSite;

    /**
     * Constructs a new {@code CookieUtils} instance with the provided default security and same-site settings.
     *
     * <hr>
     * <p>
     * 주어진 security 플래그와 same-site 설정을 기반으로 {@code CookieUtils} 인스턴스를 생성합니다.
     *
     * <hr>
     *
     * @param defaultSecure   whether cookie should be secure by default
     * @param defaultSameSite the same-site policy cookies should follow by default
     */
    public CookieUtils(String defaultDomain, Boolean defaultSecure, String defaultSameSite) {
        this.defaultDomain = defaultDomain;
        this.defaultSecure = defaultSecure;
        this.defaultSameSite = defaultSameSite;

        log.debug("CookieUtils initialized with (domain: {}, secure: {}, sameSite: {})", defaultDomain, defaultSecure, defaultSameSite);
    }

    /**
     * Constructs a new {@code CookieUtils} instance with the provided {@link CookieProperties}.
     *
     * <p>
     * Internally extracts the secure flag and same-site policy for cookies
     * and delegates to another constructor.
     *
     * <hr>
     * <p>
     * 주어진 {@link CookieProperties}를 이용하여 {@code CookieUtils} 인스턴스를 생성합니다.
     *
     * <p>
     * 내부적으로 secure와 same-site 정책의 설정값을 다른 생성자로 전달합니다.
     *
     * <hr>
     *
     * @param cookieProperties the cookie configuration properties
     * @see CookieProperties
     * @see #CookieUtils(String, Boolean, String)
     */
    public CookieUtils(CookieProperties cookieProperties) {
        this(cookieProperties.getDomain(), cookieProperties.getSecure(), cookieProperties.getSameSite());
    }

    /**
     * Retrieves a cookie by name from the given request.
     *
     * <hr>
     * <p>
     * 주어진 요청으로부터 이름에 맞는 쿠키를 조회합니다.
     *
     * <hr>
     *
     * @param exchange the server web exchange
     * @param name    the name of the cookie to retrieve
     * @return an {@link Optional} containing the cookie if found, {@link Optional#empty()} otherwise
     */
    public Optional<HttpCookie> getCookie(ServerWebExchange exchange, String name) {
        Optional<HttpCookie> optionalCookie = Optional.ofNullable(exchange.getRequest().getCookies().getFirst(name));

        optionalCookie.ifPresentOrElse(
                cookie -> log.debug("Cookie found with (name: {}, value: {})",
                        cookie.getName(),
                        cookie.getValue()),
                () -> log.debug("No cookie found with (name: {})", name)
        );

        return optionalCookie;
    }

    /**
     * Adds a cookie to the response using the provided {@link CookieAttributes}.
     *
     * <hr>
     * <p>
     * 주어진 {@link CookieAttributes}를 기반으로 생성된 쿠키를 응답에 추가합니다.
     *
     * <hr>
     *
     * @param exchange         the server web exchange
     * @param cookieAttributes the cookie attributes to apply
     */
    public void addCookie(ServerWebExchange exchange, CookieAttributes cookieAttributes) {

        exchange.getResponse().getHeaders().add("Set-Cookie", cookieAttributes.toString());
    }

    /**
     * Adds a cookie to the response with the full attribute customization.
     *
     * <hr>
     * <p>
     * 모든 속성값을 직접 설정하여 생성된 쿠키를 응답에 추가합니다.
     *
     * <hr>
     *
     * @param exchange the server web exchange
     * @param name     the name of the cookie
     * @param value    the value of the cookie
     * @param domain   the domain of the cookie
     * @param path     the path of the cookie
     * @param maxAge   the maximum age in seconds
     * @param httpOnly whether the cookie is HTTP-only
     * @param secure   whether the cookie is secure
     * @param sameSite the same-site policy
     * @throws IllegalArgumentException if {@code name} is null or {@code sameSite} is invalid
     * @see CookieAttributes#CookieAttributes(String, String, Integer, String, String, Boolean, Boolean, String)
     */
    public void addCookie(ServerWebExchange exchange, String name, String value, String domain, String path, Integer maxAge, Boolean httpOnly, Boolean secure, String sameSite) {
        CookieAttributes cookieAttributes = CookieAttributes.builder()
                .name(name)
                .value(value)
                .domain(domain)
                .path(path)
                .maxAge(maxAge)
                .secure(secure)
                .httpOnly(httpOnly)
                .sameSite(sameSite)
                .build();

        addCookie(exchange, cookieAttributes);
    }

    /**
     * Adds a cookie to the response using default secure flag and same-site policy.
     *
     * <hr>
     * <p>
     * 기본값으로 설정된 secure 속성과 same-site 정책을 기반으로 생성된 쿠키를 응답에 추가합니다.
     *
     * <hr>
     *
     * @param exchange the server web exchange
     * @param name     the name of the cookie
     * @param value    the value of the cookie
     * @param path     the path of the cookie
     * @param maxAge   the maximum age in seconds
     * @param httpOnly whether the cookie is HTTP-only
     * @throws IllegalArgumentException if {@code name} is null or {@code sameSite} is invalid
     * @see CookieAttributes#CookieAttributes(String, String, Integer, String, String, Boolean, Boolean, String)
     */
    public void addCookie(ServerWebExchange exchange, String name, String value, String path, Integer maxAge, Boolean httpOnly) {
        addCookie(exchange, name, value, defaultDomain, path, maxAge, httpOnly, defaultSecure, defaultSameSite);
    }


    /**
     * Deletes a cookie with the provided name, domain, and path.
     *
     * <hr>
     * <p>
     * 주어진 이름, 도메인, 경로에 맞는 쿠키를 삭제합니다.
     *
     * <hr>
     *
     * @param exchange the server web exchange
     * @param name     the name of the cookie to delete
     * @param domain   the domain of the cookie
     * @param path     the path of the cookie
     * @throws IllegalArgumentException if {@code name} is null
     * @see CookieAttributes#CookieAttributes(String, String, Integer, String, String, Boolean, Boolean, String)
     */
    public void deleteCookie(ServerWebExchange exchange, String name, String domain, String path) {
        CookieAttributes cookie = CookieAttributes.builder()
                .name(name)
                .domain(domain)
                .path(path)
                .maxAge(0)
                .build();
        exchange.getResponse().getHeaders().add("Set-Cookie", cookie.toString());
    }

    /**
     * Returns the default Secure flag. <hr>
     * <p>
     * Secure 플래그의 기본값을 반환합니다. <hr>
     *
     * @return the default Secure flag
     */
    // notice this can be null.
    public Boolean getDefaultSecure() {
        return this.defaultSecure;
    }

    /**
     * Returns the default SameSite attribute value. <hr>
     * <p>
     * SameSite 속성의 기본값을 반환합니다. <hr>
     *
     * @return the default SameSite attribute value
     */
    public String getDefaultSameSite() {
        return this.defaultSameSite;
    }
}
