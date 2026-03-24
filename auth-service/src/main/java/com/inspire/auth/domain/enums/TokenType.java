package com.inspire.auth.domain.enums;

import lombok.Getter;

@Getter
public enum TokenType {
    REFRESH("refresh"),
    ONETIME("onetime");

    private final String prefix;

    TokenType(String prefix) {
        this.prefix = prefix;
    }
}
