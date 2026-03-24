package com.inspire.certificate_service.ctrl;

import com.inspire.certificate_service.domain.dto.ExamDetailResponseDTO;
import com.inspire.certificate_service.domain.dto.ExamListResponseDTO;
import com.inspire.certificate_service.domain.dto.ExamSummaryResponse;
import com.inspire.certificate_service.service.ExamService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/certs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MainController {

    private final ExamService examService;

    // home 화면 API
    @GetMapping("/exams/summary")
    public ResponseEntity<ExamSummaryResponse> getExamSummary() {
        return ResponseEntity.ok(examService.getExamSummary());
    }

    // 전체보기 API (캘린더 포함?)
    @GetMapping("/exams")
    public ResponseEntity<Page<ExamListResponseDTO>> getExams(@RequestParam(name = "itemName", required = false) String itemName,
                                                              @RequestParam(name = "fieldCode", required = false) String fieldCode,
                                                              @RequestParam(name = "status", required = false) String status,
                                                              @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                              @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                              Pageable pageable) {
        Page<ExamListResponseDTO> pageResponse = examService.getExamList(itemName,
                                                                         fieldCode,
                                                                         status,
                                                                         startDate,
                                                                         endDate,
                                                                         pageable);
        return ResponseEntity.ok(pageResponse);
    }

    // 자격증 상세 조회 API
    @GetMapping("/{itemCode}")
    public ResponseEntity<ExamDetailResponseDTO> getCertificateWithSchedules(@PathVariable String itemCode) {
        return ResponseEntity.ok(examService.getCertificateWithSchedules(itemCode));
    }
}