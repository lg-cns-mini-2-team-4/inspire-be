package com.inspire.schedule_service.schedule.schedule.ctrl;

import java.time.LocalDate;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inspire.schedule_service.schedule.schedule.domain.dto.ScheduleRequestDTO;
import com.inspire.schedule_service.schedule.schedule.domain.dto.ScheduleResponseDTO;
import com.inspire.schedule_service.schedule.schedule.service.ScheduleService;

import lombok.RequiredArgsConstructor;

@Slf4j
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
    public ResponseEntity<List<ScheduleResponseDTO>> list(@RequestParam(name = "type", required = false) String type,
                                                          @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                          @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                          @RequestHeader(value = "X-User-Id") Long userId) {
        log.info("user: {}, type: {}, startDate: {}, endDate: {}", userId, type, startDate, endDate);
        List<ScheduleResponseDTO> list = scheduleService.getMySchedules(userId, type, startDate, endDate);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<ScheduleResponseDTO>> list2(@RequestHeader(name = "X-User-Id") Long userId) {
        return null;
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
    public ResponseEntity<ScheduleResponseDTO> delete(@PathVariable("id") Long id,
                                                      @RequestHeader("X-User-Id") Long userId) {
        ScheduleResponseDTO response = scheduleService.delete(id, userId);
        return ResponseEntity.ok(response);
    }
}
