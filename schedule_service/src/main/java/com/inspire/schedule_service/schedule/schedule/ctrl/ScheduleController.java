package com.inspire.schedule_service.schedule.schedule.ctrl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inspire.schedule_service.schedule.schedule.domain.dto.ScheduleRequestDTO;
import com.inspire.schedule_service.schedule.schedule.domain.dto.ScheduleResponseDTO;
import com.inspire.schedule_service.schedule.schedule.service.ScheduleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    public final ScheduleService scheduleService;

    // 1. 개인 일정 등록
    @PostMapping("")
    public ResponseEntity<Void> create(@RequestBody ScheduleRequestDTO request,
                                       @RequestHeader("X-User-Id") Long userId) {
        scheduleService.create(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 3. 전체 일정 목록 조회 (달력용)
    @GetMapping("")
    public ResponseEntity<List<ScheduleResponseDTO>> list(@RequestParam(name = "startDate", required = false) LocalDate startDate,
                                                          @RequestParam(name = "endDate", required = false) LocalDate endDate,
                                                          @RequestHeader(value = "X-User-Id") Long userId) {
        List<ScheduleResponseDTO> list = scheduleService.getMySchedules(userId, startDate, endDate);
        return ResponseEntity.ok(list);
    }

    // 4. 일정 상세 읽기
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDTO> read(@PathVariable("id") Long id,
                                                    @RequestHeader("X-User-Id") Long userId) {
        ScheduleResponseDTO response = scheduleService.read(id, userId);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    // 6. 일정 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id,
                                       @RequestHeader("X-User-Id") Long userId) {
        scheduleService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }
}
