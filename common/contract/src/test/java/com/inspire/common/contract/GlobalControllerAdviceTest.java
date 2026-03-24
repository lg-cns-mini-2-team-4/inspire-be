package com.inspire.common.contract;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GlobalControllerAdviceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testResponseBodyAdvice() throws Exception {
        mockMvc.perform(get("/method1"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/method2"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/method3"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/method4"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/method5"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/method6"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/method7"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/method8"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/method9"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/method10"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/method11"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/method12"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testResponseBodyAdvice2() throws Exception {
        mockMvc.perform(get("/api1"))
                .andDo(print());

        mockMvc.perform(get("/api2"))
                .andDo(print());

        mockMvc.perform(get("/api3"))
                .andDo(print());

        mockMvc.perform(get("/api4"))
                .andDo(print());

        mockMvc.perform(get("/api5"))
                .andDo(print());

        mockMvc.perform(get("/api6"))
                .andDo(print());

        mockMvc.perform(get("/api7"))
                .andDo(print());

        mockMvc.perform(get("/api8"))
                .andDo(print());

        mockMvc.perform(get("/api9"))
                .andDo(print());

        mockMvc.perform(get("/api10"))
                .andDo(print());

        mockMvc.perform(get("/api11"))
                .andDo(print());

        mockMvc.perform(get("/api12"))
                .andDo(print());

        mockMvc.perform(get("/api13"))
                .andDo(print());

        mockMvc.perform(get("/api14"))
                .andDo(print());
    }

    @Test
    void testValidation() throws Exception {

        String json = "[{\"message\": \"\"}, {\"message\": \"\"}, {\"message\": \"\"}]";
        String urlencoded = "message=";

        mockMvc.perform(get("/validation1")
                        .header("Accept-Language", "ko")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print());

        mockMvc.perform(get("/validation2")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .header("Accept-Language", "ko")
                        .content(urlencoded))
                .andDo(print());

        mockMvc.perform(get("/validation3/123?my=ye")
                        .cookie(new Cookie("test", "10"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andDo(print());
    }

    @SpringBootApplication
    static class TestApplication {

    }
}

// NoResourceFoundException
