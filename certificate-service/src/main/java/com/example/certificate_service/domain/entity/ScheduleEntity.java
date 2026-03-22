package com.example.certificate_service.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "schedule")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId; // 스케줄 번호 (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_code")
    private CertificateEntity certificate; // 종목 코드 (FK)

    @Column(name = "impl_year")
    private String implYear; // 시행년도

    @Column(name = "impl_seq")
    private Integer implSeq; // 시행회차

    @Column(name = "written_reg_start")
    private LocalDate writtenRegStart; // 필기시험 원서접수 시작

    @Column(name = "written_reg_end")
    private LocalDate writtenRegEnd; // 필기시험 원서접수 종료

    @Column(name = "written_exam_start")
    private LocalDate writtenExamStart; // 필기시험 시작

    @Column(name = "written_exam_end")
    private LocalDate writtenExamEnd; // 필기시험 종료

    @Column(name = "written_pass_date")
    private LocalDate writtenPassDate; // 필기시험 합격 발표일

    @Column(name = "practical_reg_start")
    private LocalDate practicalRegStart; // 실기시험 원서접수 시작

    @Column(name = "practical_reg_end")
    private LocalDate practicalRegEnd; // 실기시험 원서접수 종료

    @Column(name = "practical_exam_start")
    private LocalDate practicalExamStart; // 실기 시작

    @Column(name = "practical_exam_end")
    private LocalDate practicalExamEnd; // 실기 종료

    @Column(name = "practical_pass_date")
    private LocalDate practicalPassDate; // 실기시험 합격 발표일

    @Column(name = "description")
    private String description; // 설명

    // @Column(name = "office_code")
    // private String officeCode; // 지사코드

    // @Column(name = "office_name")
    // private String officeName; // 지사명

    // @Column(name = "exam_location")
    // private String examLocation; // 시행장소

    // @Column(name = "location_number")
    // private String locationNumber; // 전화번호

}