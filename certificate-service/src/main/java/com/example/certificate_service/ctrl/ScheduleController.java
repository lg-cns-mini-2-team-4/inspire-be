package com.example.certificate_service.ctrl;

import com.example.certificate_service.domain.dto.ScheduleActiveResponseDTO;
import com.example.certificate_service.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/schedules") // API 게이트웨이 또는 프론트엔드 라우팅 규칙에 맞게 조정해 주세요.
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/active")
    public ResponseEntity<List<ScheduleActiveResponseDTO>> getActiveSchedules() {
        List<ScheduleActiveResponseDTO> response = scheduleService.getActiveSchedules();
        return ResponseEntity.ok(response);
    }
}