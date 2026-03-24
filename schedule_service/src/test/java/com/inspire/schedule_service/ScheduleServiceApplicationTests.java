package com.inspire.schedule_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inspire.schedule_service.schedule.schedule.dao.ScheduleRepository;
import com.inspire.schedule_service.schedule.schedule.domain.entity.EventType;
import com.inspire.schedule_service.schedule.schedule.domain.entity.ScheduleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ScheduleServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ScheduleRepository repository;

	@BeforeEach
	void setUp() {
		repository.deleteAll();

		ScheduleEntity exam1 = ScheduleEntity.builder()
				.userId(1L)
				.title("t1")
				.type(EventType.EXAM)
				.date(LocalDate.of(2026, 3, 24))
				.description("test")
				.refId("3L")
				.build();

		ScheduleEntity exam2 = ScheduleEntity.builder()
				.userId(1L)
				.title("t1")
				.type(EventType.EXAM)
				.date(LocalDate.of(2026, 3, 28))
				.description("test")
				.refId("3L")
				.build();


		ScheduleEntity exam3 = ScheduleEntity.builder()
				.userId(1L)
				.title("t1")
				.type(EventType.EXAM)
				.date(LocalDate.of(2026, 3, 30))
				.description("test")
				.refId("3L")
				.build();

		repository.save(exam1);
		repository.save(exam2);
		repository.save(exam3);
	}

	@Test
	void pagingTest() throws Exception {

		// 24, 28, 30
		mockMvc.perform(get("/schedules")
						.header("X-User-Id", 1))
				.andDo(print());

		mockMvc.perform(get("/schedules?startDate=2026-03-24&endDate=2026-03-28")
						.header("X-User-Id", 1))
				.andDo(print());

		mockMvc.perform(get("/schedules?startDate=2026-03-28")
						.header("X-User-Id", 1))
				.andDo(print());

		mockMvc.perform(get("/schedules?endDate=2026-03-28")
						.header("X-User-Id", 1))
				.andDo(print());
	}
}
