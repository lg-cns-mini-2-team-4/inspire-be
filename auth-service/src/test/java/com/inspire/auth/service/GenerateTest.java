package com.inspire.auth.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateTest {
    @Test
    void encodePassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String[] passwords = {"password1!", "password2!", "password3!", "password4!", "password5!"};
        for(String password: passwords) {
            System.out.println(password);
            System.out.println(encoder.encode(password));
        }
    }
}
