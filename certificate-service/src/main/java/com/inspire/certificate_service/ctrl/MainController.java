// package com.example.certificate_service.ctrl;

// import com.example.certificate_service.domain.dto.*;
// import com.example.certificate_service.service.CertificateService;
// import com.example.certificate_service.service.ScheduleService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/certificate-service")
// @RequiredArgsConstructor
// @CrossOrigin(origins = "*") // 모든 프론트엔드 요청 허용 (필요에 따라 특정 도메인으로 제한 가능)
// public class MainController {

//     private final CertificateService certificateService;
//     private final ScheduleService scheduleService;

//     /* Home */
//     // 1. 현재 접수 중인 시험 조회
//     @GetMapping("/active")
//     public ResponseEntity<List<ScheduleActiveResponseDTO>> getActiveSchedules() {
//         List<ScheduleActiveResponseDTO> response = scheduleService.getActiveSchedules();
//         return ResponseEntity.ok(response);
//     }

//     // 2. 다가오는 시험 조회
//     @GetMapping("/upcoming")
//     public ResponseEntity<List<ScheduleActiveResponseDTO>> getUpcomingSchedules() {
//         List<ScheduleActiveResponseDTO> response = scheduleService.getUpcomingSchedules();
//         return ResponseEntity.ok(response);
//     }

//     // 3. 대시보드 상태별 시험 개수 조회
//     @GetMapping("/status-counts")
//     public ResponseEntity<ScheduleStatusCountResponseDTO> getScheduleStatusCounts() {
//         ScheduleStatusCountResponseDTO response = scheduleService.getScheduleStatusCounts();
//         return ResponseEntity.ok(response);
//     }


//     /* CalendarView */
//     // 4. 특정 연도 시험 일정 조회
//     @GetMapping("/calendar")
//     public ResponseEntity<List<ScheduleCalendarResponseDTO>> getSchedulesForCalendar(@RequestParam(name = "year") String year) {
//         List<ScheduleCalendarResponseDTO> response = scheduleService.getSchedulesByYearForCalendar(year);
//         return ResponseEntity.ok(response);
//     }


//     /* ExamList */
//     // 5. 시험 일정 통합 검색 및 필터링
//     @GetMapping("/exams")
//     public ResponseEntity<List<ScheduleActiveResponseDTO>> getExams(@RequestParam(required = false) String status) {
//         if ("접수중".equals(status)) {
//             List<ScheduleActiveResponseDTO> activeExams = scheduleService.getActiveExamsAll();
//             return ResponseEntity.ok(activeExams);
//         }   // [수정 필요] 로직 추가
//         List<ScheduleActiveResponseDTO> allExams = scheduleService.getAllSchedules();
//         return ResponseEntity.ok(allExams);
//     }

//     /* ExamDetail */

//     // 6. 자격증 상세 정보 조회
//     @GetMapping("/{itemCode}")
//     public ResponseEntity<CertificateDetailResponseDTO> getCertificateDetail(@PathVariable String itemCode) {
//         return ResponseEntity.ok(certificateService.getCertificateDetail(itemCode));
//     }

//     // 7. 자격증 상세 정보 + 시험 일정 통합 조회
//     @GetMapping("/{itemCode}/detail")
//     public ResponseEntity<CertificateWithSchedulesDTO> getCertificateWithSchedules(@PathVariable String itemCode) {
//         return ResponseEntity.ok(certificateService.getCertificateWithSchedules(itemCode));
//     }

//     // 8. 자격증 및 시험 일정 동기화 (관리자용)
//     @PostMapping("/sync")
//     public ResponseEntity<String> syncCertificates() {
//         certificateService.syncCertificates();
//         return ResponseEntity.ok("자격증 동기화 완료");
//     }

//     // 9. 시험 일정 동기화 (관리자용) - 연도별로 관리 가능하도록 수정
//     @PostMapping("/sync/schedules")
//     public ResponseEntity<String> syncSchedules() {
//         // 하드코딩된 "2026" 대신 동적으로 관리 가능하나, 기존 로직 유지
//         certificateService.syncSchedules("2026");
//         return ResponseEntity.ok("2026년 시험일정 동기화 완료");
//     }

// }

package com.inspire.certificate_service.ctrl;

import com.inspire.certificate_service.domain.dto.ExamDetailResponseDTO;
import com.inspire.certificate_service.domain.dto.ExamListResponseDTO;
import com.inspire.certificate_service.domain.dto.ExamSummaryResponse;
import com.inspire.certificate_service.service.ExamService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/certs")
@RequiredArgsConstructor
public class MainController {

    private final ExamService examService;

    @GetMapping("/exams/summary")
    public ResponseEntity<ExamSummaryResponse> getExamSummary() {
        return ResponseEntity.ok(examService.getExamSummary());
    }

    // MainController.java 에 추가

    @GetMapping("/exams")
    public ResponseEntity<Page<ExamListResponseDTO>> getExams(
            @RequestParam(name = "itemName", required = false) String itemName,
            @RequestParam(name = "fieldCode", required = false) String fieldCode,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "startDate", required = false) LocalDate startDate, // 추가
            @RequestParam(name = "endDate", required = false) LocalDate endDate,   // 추가
            Pageable pageable
    ) {
//
//        // 1. 페이징 정보가 없는 경우 (startDate, endDate 기반 달력 조회)
//        // Pageable의 파라미터(page, size)가 요청에 없으면 기본값이 들어가는데,
//        // 이를 명확히 구분하기 위해 startDate/endDate 존재 여부로 판단합니다.
//        if (startDate != null && endDate != null) {
//            List<ExamListResponseDTO> listResponse = examService.getExamListByRange(
//                    itemName, fieldCode, status, startDate, endDate);
//            return ResponseEntity.ok(listResponse);
//        }

        // 2. 페이징 정보가 있는 경우 (기존 기능 유지)
        Page<ExamListResponseDTO> pageResponse = examService.getExamList(
                itemName, fieldCode, status, startDate, endDate, pageable);
        return ResponseEntity.ok(pageResponse);
    }

    // MainController.java 에 추가
    @GetMapping("/{itemCode}")
    public ResponseEntity<ExamDetailResponseDTO> getCertificateWithSchedules(@PathVariable String itemCode) {
        return ResponseEntity.ok(examService.getCertificateWithSchedules(itemCode));
    }
}