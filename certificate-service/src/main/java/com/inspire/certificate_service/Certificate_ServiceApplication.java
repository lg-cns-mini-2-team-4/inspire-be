package com.inspire.certificate_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

@SpringBootApplication
// @EnableScheduling  // 스케줄러 활성화
public class Certificate_ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Certificate_ServiceApplication.class, args);
    }

    @Bean
    public XmlMapper xmlMapper() {
        return new XmlMapper();
    }

@Bean
public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
}
}
