// package com.example.certificate_service.domain.entity;

// import jakarta.persistence.*;
// import lombok.*;
// import java.time.LocalDate;

// @Entity
// @Table(name = "schedule")
// @Getter
// @NoArgsConstructor(access = AccessLevel.PROTECTED)
// @AllArgsConstructor
// @Builder
// public class ScheduleEntity {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     @Column(name = "schedule_id")
//     private Long scheduleId; // 스케줄 번호 (PK)

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "item_code")
//     private CertificateEntity certificate; // 종목 코드 (FK)

//     @Column(name = "impl_year")
//     private String implYear; // 시행년도

//     @Column(name = "impl_seq")
//     private Integer implSeq; // 시행회차

//     @Column(name = "written_reg_start")
//     private LocalDate writtenRegStart; // 필기시험 원서접수 시작

//     @Column(name = "written_reg_end")
//     private LocalDate writtenRegEnd; // 필기시험 원서접수 종료

//     @Column(name = "written_exam_start")
//     private LocalDate writtenExamStart; // 필기시험 시작

//     @Column(name = "written_exam_end")
//     private LocalDate writtenExamEnd; // 필기시험 종료

//     @Column(name = "written_pass_date")
//     private LocalDate writtenPassDate; // 필기시험 합격 발표일

//     @Column(name = "practical_reg_start")
//     private LocalDate practicalRegStart; // 실기시험 원서접수 시작

//     @Column(name = "practical_reg_end")
//     private LocalDate practicalRegEnd; // 실기시험 원서접수 종료

//     @Column(name = "practical_exam_start")
//     private LocalDate practicalExamStart; // 실기 시작

//     @Column(name = "practical_exam_end")
//     private LocalDate practicalExamEnd; // 실기 종료

//     @Column(name = "practical_pass_date")
//     private LocalDate practicalPassDate; // 실기시험 합격 발표일

//     @Column(name = "description")
//     private String description; // 설명

// }

package com.inspire.certificate_service.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "exam")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ExamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_id")
    private Long examId; // 시험 일정 번호 (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_code")
    private CertificateEntity certificate;  // 종목 코드 (FK)

    @Column(name = "impl_year")
    private String implYear;                // 시행년도

    @Column(name = "impl_seq")
    private Integer implSeq;                // 시행회차

    @Column(name = "type")
    private String type;                    // 시험 유형 (WR, WE, PR, PE, PD)
                                            // WR: 필기 원서접수, WE: 필기 시험, PR: 실기 원서접수, PE: 실기 시험, PD: 합격 발표

    @Column(name = "start_date")
    private LocalDate startDate;            // 시험 시작일

    @Column(name = "end_date")
    private LocalDate endDate;              // 시험 종료일

    @Column(name = "description")
    private String description;             // 설명

}