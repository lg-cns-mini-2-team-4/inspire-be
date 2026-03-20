package com.inspire.common.cookie.servlet;

import java.util.Arrays;
import java.util.Optional;

import com.inspire.common.cookie.model.CookieAttributes;
import com.inspire.common.cookie.config.CookieProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for managing HTTP cookies in servlet-based applications.
 *
 * <p>
 * Provides convenience methods for retrieving, adding, and deleting cookies
 * using {@link HttpServletRequest} and {@link HttpServletResponse}.
 * Supports both direct cookie attributes and the {@link CookieAttributes}.
 *
 * <hr>
 * <p>
 * 서블릿 기반 애플리케이션에서 HTTP 쿠키를 관리하기 위한 유틸리티 클래스.
 *
 * <p>
 * {@link HttpServletRequest} 및 {@link HttpServletResponse} 기반으로,
 * 쿠키의 조회, 추가, 삭제를 위한 기능을 제공하는 클래스입니다.
 *
 * <hr>
 *
 * @author Wooseong Urm
 * @see HttpServletRequest
 * @see HttpServletResponse
 * @see CookieAttributes
 * @since 1.0.0
 */
@Slf4j
@Getter
public class CookieUtils {

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

    private final String defaultDomain;

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
     * @param request the HTTP servlet request
     * @param name    the name of the cookie to retrieve
     * @return an {@link Optional} containing the cookie if found, {@link Optional#empty()} otherwise
     */
    public Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Optional<Cookie> optionalCookie = Optional.ofNullable(request.getCookies())
                .flatMap(cookies -> Arrays.stream(cookies)
                        .filter(cookie -> cookie.getName().equals(name))
                        .findFirst());

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
     * @param response         the HTTP servlet response
     * @param cookieAttributes the cookie attributes to apply
     */
    public void addCookie(HttpServletResponse response, CookieAttributes cookieAttributes) {
        response.addHeader("Set-Cookie", cookieAttributes.toString());
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
     * @param response the HTTP servlet response
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
    public void addCookie(HttpServletResponse response, String name, String value, String domain, String path, Integer maxAge, Boolean httpOnly, Boolean secure, String sameSite) {
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
        addCookie(response, cookieAttributes);
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
     * @param response the HTTP servlet response
     * @param name     the name of the cookie
     * @param value    the value of the cookie
     * @param path     the path of the cookie
     * @param maxAge   the maximum age in seconds
     * @param httpOnly whether the cookie is HTTP-only
     * @throws IllegalArgumentException if {@code name} is null or {@code sameSite} is invalid
     * @see CookieAttributes#CookieAttributes(String, String, Integer, String, String, Boolean, Boolean, String)
     */
    public void addCookie(HttpServletResponse response, String name, String value, String path, Integer maxAge, Boolean httpOnly) {
        addCookie(response, name, value, defaultDomain, path, maxAge, httpOnly, defaultSecure, defaultSameSite);
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
     * @param response the HTTP servlet response
     * @param name     the name of the cookie to delete
     * @param domain   the domain of the cookie
     * @param path     the path of the cookie
     * @throws IllegalArgumentException if {@code name} is null
     * @see CookieAttributes#CookieAttributes(String, String, Integer, String, String, Boolean, Boolean, String)
     */
    public void deleteCookie(HttpServletResponse response, String name, String domain, String path) {
        CookieAttributes cookie = CookieAttributes.builder()
                .name(name)
                .domain(domain)
                .path(path)
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void deleteCookie(HttpServletResponse response, String name, String path) {
        CookieAttributes cookie = CookieAttributes.builder()
                .name(name)
                .domain(defaultDomain)
                .path(path)
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
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

    public String getDefaultDomain() {
        return this.defaultDomain;
    }
}
