package com.example.certificate_service.ctrl;

import com.example.certificate_service.domain.dto.ScheduleActiveResponseDTO;
import com.example.certificate_service.domain.dto.ScheduleCalendarResponseDTO;
import com.example.certificate_service.domain.dto.ScheduleStatusCountResponseDTO;
import com.example.certificate_service.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // 리액트 주소를 허용합니다.
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 1. home - 현재 접수 중인 시험
    @GetMapping("/active")
    public ResponseEntity<List<ScheduleActiveResponseDTO>> getActiveSchedules() {
        List<ScheduleActiveResponseDTO> response = scheduleService.getActiveSchedules();
        return ResponseEntity.ok(response);
    }

    // 2. home - 다가오는 시험
    @GetMapping("/upcoming")
    public ResponseEntity<List<ScheduleActiveResponseDTO>> getUpcomingSchedules() {
        List<ScheduleActiveResponseDTO> response = scheduleService.getUpcomingSchedules();
        return ResponseEntity.ok(response);
    }

    // 3. home - 대시보드 상태별 시험 개수 조회
    @GetMapping("/status-counts")
    public ResponseEntity<ScheduleStatusCountResponseDTO> getScheduleStatusCounts() {
        ScheduleStatusCountResponseDTO response = scheduleService.getScheduleStatusCounts();
        return ResponseEntity.ok(response);
    }

    // 4. calendar - 특정 연도 시험 일정 조회
    @GetMapping("/calendar")
    public ResponseEntity<List<ScheduleCalendarResponseDTO>> getSchedulesForCalendar(@RequestParam(name = "year") String year) {        
        List<ScheduleCalendarResponseDTO> response = scheduleService.getSchedulesByYearForCalendar(year);
        return ResponseEntity.ok(response);
    }


}