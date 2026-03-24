package com.inspire.common.cookie.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Holds cookie-related configuration properties.
 *
 * <p>
 * Maps settings with the {@code cookie} prefix in {@code application.yml} or {@code application.properties}.
 * Manages the secure flag and same-site policy of cookies.
 *
 * <hr>
 * <p>
 * 쿠키 관련 설정 정보를 담는 프로퍼티 클래스.
 *
 * <p>
 * {@code application.yml} 또는 {@code application.properties}의 {@code cookie} 접두사를 가진 값을 매핑합니다.
 * 쿠키의 secure 설정과 same-site policy를 관리합니다.
 *
 * <hr>
 *
 * @author Wooseong Urm
 * @since 1.0.0
 */
@Slf4j
@ConfigurationProperties(prefix = "cookie")
public class CookieProperties {

    private final String domain;
    /**
     * Secure flag. <hr>
     * Secure 플래그.
     */
    private final Boolean secure;
    /**
     * SameSite attribute value.
     *
     * <p>
     * This must be one of {@code Strict}, {@code Lax}, or {@code None}, case-insensitively, or {@code null}.
     *
     * <hr>
     * <p>
     * SameSite 속성값.
     *
     * <p>
     * 대소문자 구분 없이 {@code Lax}, {@code Strict}, {@code None} 중 하나이거나 {@code null} 값이어야 합니다.
     *
     */
    private final String sameSite;

    /**
     * Constructs a new {@code CookieProperties} instance with the provided secure and sameSite values.
     *
     * <hr>
     * <p>
     * 지정된 secure과 sameSite 값을 기반으로 {@code CookieProperties} 인스턴스를 생성합니다.
     *
     * <hr>
     *
     * @param secure   the Secure flag
     * @param sameSite the SameSite attribute value
     */
    public CookieProperties(String domain, Boolean secure, String sameSite) {
        this.domain = domain;
        this.secure = secure;
        this.sameSite = sameSite;
        log.debug("CookieProperties initialized with (domain: {}, secure: {}, sameSite: {})", domain, secure, sameSite);
    }

    public String getDomain() {
        return this.domain;
    }

    /**
     * Returns the Secure flag. <hr>
     * <p>
     * Secure 플래그를 반환합니다. <hr>
     *
     * @return the Secure flag
     */
    // notice this can be null.
    public Boolean getSecure() {
        return this.secure;
    }

    /**
     * Returns the SameSite attribute value. <hr>
     * <p>
     * SameSite 속성값을 반환합니다. <hr>
     *
     * @return the SameSite attribute value
     */
    public String getSameSite() {
        return this.sameSite;
    }

}
