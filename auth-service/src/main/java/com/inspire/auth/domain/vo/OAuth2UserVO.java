package com.inspire.auth.domain.vo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
