package com.inspire.auth.domain.vo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class OAuth2UserVO {
    // String (nick)name, String email
    // String external id, String provider,
    private String externalId;
    private String name;
    private String email;
    private String provider;
}
