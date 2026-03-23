// package com.example.certificate_service.ctrl;

// import com.example.certificate_service.domain.dto.CertificateDetailResponseDTO;
// import com.example.certificate_service.service.CertificateService;
// import lombok.RequiredArgsConstructor;
// import com.example.certificate_service.domain.dto.CertificateWithSchedulesDTO;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// @CrossOrigin(origins = "http://localhost:5173")
// @RestController
// @RequestMapping("/api/certificates")
// @RequiredArgsConstructor
// public class CertificateController {

//     private final CertificateService certificateService;

//     // 자격증 상세 조회
//     @GetMapping("/{itemCode}")  
//     public ResponseEntity<CertificateDetailResponseDTO> getCertificateDetail(
//             @PathVariable String itemCode) {
//         return ResponseEntity.ok(certificateService.getCertificateDetail(itemCode));
//     }

//     @PostMapping("/sync")
//     public ResponseEntity<String> syncNow() {
//         certificateService.syncCertificates();
//         return ResponseEntity.ok("동기화 완료");
//     }

//         // 임시 동기화 테스트용
//     @PostMapping("/sync/schedules")
//     public ResponseEntity<String> syncSchedules() {
//         certificateService.syncSchedules("2026");
//         return ResponseEntity.ok("시험일정 동기화 완료");
//     }

//     // 자격증 + 시험일정 통합 조회 (ExamDetail용)
//     @GetMapping("/{itemCode}/detail")
//     public ResponseEntity<CertificateWithSchedulesDTO> getCertificateWithSchedules(
//             @PathVariable String itemCode) {
//         return ResponseEntity.ok(certificateService.getCertificateWithSchedules(itemCode));
// }
// }