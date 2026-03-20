package com.example.certificate_service.ctrl;

import com.example.certificate_service.domain.dto.ScheduleActiveResponseDTO;
import com.example.certificate_service.domain.dto.ScheduleCalendarResponseDTO;
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
@RequestMapping("/api/schedules") // API 게이트웨이 또는 프론트엔드 라우팅 규칙에 맞게 조정해 주세요.
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // 리액트 주소를 허용합니다.
public class ScheduleController {

    private final ScheduleService scheduleService;

    // [기능 1] 현재 접수중인 시험 API
    @GetMapping("/active")
    public ResponseEntity<List<ScheduleActiveResponseDTO>> getActiveSchedules() {
        List<ScheduleActiveResponseDTO> response = scheduleService.getActiveSchedules();
        return ResponseEntity.ok(response);
    }

    // [기능 2] 다가오는 시험 API
    @GetMapping("/upcoming")
    public ResponseEntity<List<ScheduleActiveResponseDTO>> getUpcomingSchedules() {
        List<ScheduleActiveResponseDTO> response = scheduleService.getUpcomingSchedules();
        return ResponseEntity.ok(response);
    }

// [기능 3 수정] 캘린더용 특정 연도 시험 일정 조회 API
    @GetMapping("/calendar")
    public ResponseEntity<List<ScheduleCalendarResponseDTO>> getSchedulesForCalendar(
            @RequestParam(name = "year") String year) {
        
        List<ScheduleCalendarResponseDTO> response = scheduleService.getSchedulesByYearForCalendar(year);
        return ResponseEntity.ok(response);
    }
}