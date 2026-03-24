package com.inspire.schedule_service.schedule.schedule.ctrl;

import com.inspire.schedule_service.schedule.schedule.dao.ScheduleRepository;
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

import com.inspire.schedule_service.schedule.schedule.domain.dto.ScheduleRequestDTO;
import com.inspire.schedule_service.schedule.schedule.domain.dto.ScheduleResponseDTO;
import com.inspire.schedule_service.schedule.schedule.service.ScheduleService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    public final ScheduleService scheduleService;

    // 1. 일정 등록
    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody List<ScheduleRequestDTO> request, 
                                     @RequestHeader("X-User-Id") Long userId) {
        scheduleService.create(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 2. 전체 일정 목록 조회 (달력용)
    @GetMapping("/me")
    public ResponseEntity<List<ScheduleResponseDTO>> getMySchedules(@RequestHeader("X-User-Id") Long userId) {
        
        List<ScheduleResponseDTO> response = scheduleService.getMySchedules(userId);
        
        return ResponseEntity.ok(response);
    }

    // 3. 일정 상세 읽기
    @GetMapping("/read/{id}")
    public ResponseEntity<ScheduleResponseDTO> read(@PathVariable("id") Long id,
                                                  @RequestHeader("X-User-Id") Long userId) {
        ScheduleResponseDTO response = scheduleService.read(id, userId);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    // 4. 일정 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id, 
                                      @RequestHeader("X-User-Id") Long userId) {
        scheduleService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    // 5. 자격증 즐겨찾기 취소 시 일괄삭제
    @DeleteMapping("/delete-all/{itemCode}")
    public ResponseEntity<Void> deleteAllByFavorite( @PathVariable("itemCode") String itemCode, 
                                                    @RequestHeader("X-User-Id") Long userId) {
        
        scheduleService.deleteAllByFavorite(userId, itemCode);
        
        return ResponseEntity.noContent().build();
    }
}
