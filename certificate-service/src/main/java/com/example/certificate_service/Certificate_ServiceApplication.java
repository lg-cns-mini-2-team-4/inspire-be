package com.example.certificate_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Certificate_ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Certificate_ServiceApplication.class, args);
    }
}
