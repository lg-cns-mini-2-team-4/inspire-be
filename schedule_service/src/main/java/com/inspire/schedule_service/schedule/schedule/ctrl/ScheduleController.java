package com.inspire.schedule_service.schedule.schedule.ctrl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inspire.schedule_service.schedule.schedule.domain.dto.ExamScheduleRequestDTO;
import com.inspire.schedule_service.schedule.schedule.domain.dto.ScheduleRequestDTO;
import com.inspire.schedule_service.schedule.schedule.domain.dto.ScheduleResponseDTO;
import com.inspire.schedule_service.schedule.schedule.service.ScheduleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    public final ScheduleService scheduleService;

    // 1. 개인 일정 수동 등록
    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody ScheduleRequestDTO request, 
                                     @RequestHeader("X-User-Id") Long userId) {
        scheduleService.create(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 2. 시험 일정 자동 등록
    @PostMapping("/register-exam")
    public ResponseEntity<Void> registerExam(@RequestBody ExamScheduleRequestDTO request, 
                                        @RequestHeader("X-User-Id") Long userId) {
        scheduleService.registerExamSchedules(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 3. 전체 일정 목록 조회 (달력용)
    @GetMapping("/list")
    public ResponseEntity<List<ScheduleResponseDTO>> list(@RequestHeader("X-User-Id") Long userId) {
        List<ScheduleResponseDTO> list = scheduleService.list(userId);
        return ResponseEntity.ok(list);
    }

    // 4. 일정 상세 읽기
    @GetMapping("/read/{id}")
    public ResponseEntity<ScheduleResponseDTO> read(@PathVariable("id") Long id,
                                                  @RequestHeader("X-User-Id") Long userId) {
        ScheduleResponseDTO response = scheduleService.read(id, userId);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    // 5. 일정 수정
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, 
                                      @RequestBody ScheduleRequestDTO request,
                                      @RequestHeader("X-User-Id") Long userId) {
        ScheduleResponseDTO updated = scheduleService.update(id, request, userId);
        return updated != null ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // 6. 일정 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id, 
                                      @RequestHeader("X-User-Id") Long userId) {
        scheduleService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }
}
