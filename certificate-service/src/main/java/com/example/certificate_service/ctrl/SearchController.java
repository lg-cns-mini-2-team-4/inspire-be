package com.example.certificate_service.ctrl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.certificate_service.domain.dto.ScheduleActiveResponseDTO;
import com.example.certificate_service.service.ScheduleService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*") // 프론트엔드 연동을 위해 추가
@RestController
@RequestMapping("/exams")
@RequiredArgsConstructor
public class SearchController {
    
    private final ScheduleService scheduleService;

    /**
     * 시험 일정 조회 및 필터링
     * 1. GET /exams : 전체 시험 일정 조회 | 반환 타입: ScheduleActiveResponseDTO
     * 2. GET /exams?status=접수중 : 현재 접수 중인 시험만 조회 | 반환 타입: ScheduleAllResponseDTO
     */
    @GetMapping
    public ResponseEntity<List<ScheduleActiveResponseDTO>> getExams(
        @RequestParam(required = false) String status
    ) {
        // 여기서 status가 null인지 "접수중"인지에 따라 처리
        if ("접수중".equals(status)) {
            System.out.println(">>> Search Controller Active Exams");
            List<ScheduleActiveResponseDTO> activeExams = scheduleService.getActiveExamsAll();
            return ResponseEntity.ok(activeExams);
        }
        // 전체 시험 
        System.out.println(">>> Search Controller All Exams");
        List<ScheduleActiveResponseDTO> allExams = scheduleService.getAllSchedules();
        return ResponseEntity.ok(allExams);
    }
}
