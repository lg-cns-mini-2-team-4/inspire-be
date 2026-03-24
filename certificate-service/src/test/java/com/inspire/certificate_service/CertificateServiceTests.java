package com.inspire.certificate_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inspire.certificate_service.dao.CertificateRepository;
import com.inspire.certificate_service.dao.ExamRepository;
import com.inspire.certificate_service.domain.entity.CertificateEntity;
import com.inspire.certificate_service.domain.entity.ExamEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class CertificateServiceTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CertificateRepository certificateRepository;

    @BeforeEach
    void setUp() {

        CertificateEntity ce = CertificateEntity.builder()
                .itemCode("Icode1")
                .itemName("Iname1")
                .certTypeCode("Ccode1")
                .certTypeName("Cname1")
                .seriesCode("Scode1")
                .seriesName("Sname1")
                .build();

        certificateRepository.save(ce);
        ExamEntity exam1 = ExamEntity.builder()
                .certificate(ce)
                .implYear("2026")
                .implSeq(3)
                .type("WR")
                .startDate(LocalDate.of(2026, 3, 22))
                .endDate(LocalDate.of(2026, 3, 25))
                .description("d1")
                .build();

        ExamEntity exam2 = ExamEntity.builder()
                .certificate(ce)
                .implYear("2026")
                .implSeq(3)
                .type("WR")
                .startDate(LocalDate.of(2026, 3, 24))
                .endDate(LocalDate.of(2026, 4, 2))
                .description("d1")
                .build();

        ExamEntity exam3 = ExamEntity.builder()
                .certificate(ce)
                .implYear("2026")
                .implSeq(3)
                .type("WR")
                .startDate(LocalDate.of(2026, 3, 31))
                .endDate(LocalDate.of(2026, 4, 8))
                .description("d1")
                .build();

        ExamEntity exam4 = ExamEntity.builder()
                .certificate(ce)
                .implYear("2026")
                .implSeq(3)
                .type("WR")
                .startDate(LocalDate.of(2026, 3, 10))
                .endDate(LocalDate.of(2026, 3, 15))
                .description("d1")
                .build();

        examRepository.save(exam1);
        examRepository.save(exam2);
        examRepository.save(exam3);
        examRepository.save(exam4);
    }

    @Test
    void pagingTest() throws Exception {

        // 3/22~3/25(active), 3/24~4/2(active), 3/31~4/8 (upcoming)
        mockMvc.perform(get("/certs/exams/summary"))
                .andDo(print());

        mockMvc.perform(get("/certs/exams?status=active"))
                .andDo(print());

        mockMvc.perform(get("/certs/exams?status=active&startDate=2026-03-20"))
                .andDo(print());

        mockMvc.perform(get("/certs/exams?endDate=2026-03-28"))
                .andDo(print());

        mockMvc.perform(get("/certs/exams?status=active&startDate=2026-03-20&endDate=2026-03-28"))
                .andDo(print());

        mockMvc.perform(get("/certs/exams?status=upcoming&page=0&size=1&sort=startDate,desc"))
                .andDo(print());

        mockMvc.perform(get("/certs/Icode1"))
                .andDo(print());
    }
}
