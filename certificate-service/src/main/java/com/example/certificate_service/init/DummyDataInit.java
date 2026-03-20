package com.example.certificate_service.init;

import com.example.certificate_service.dao.CertificateRepository;
import com.example.certificate_service.dao.ScheduleRepository;
import com.example.certificate_service.domain.entity.CertificateEntity;
import com.example.certificate_service.domain.entity.ScheduleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DummyDataInit implements CommandLineRunner {

    private final CertificateRepository certificateRepository;
    private final ScheduleRepository scheduleRepository;

    @Override
    public void run(String... args) throws Exception {
        // 기존 데이터가 있다면 덮어쓰지 않도록 방어 로직
        if (certificateRepository.count() > 0) {
            System.out.println(">>> 더미 데이터가 이미 존재합니다. 생성을 스킵합니다.");
            return;
        }

        System.out.println(">>> 🚀 실기시험 포함 더미 데이터 생성을 시작합니다...");

        // 1. 자격증(Certificate) 데이터 생성
        CertificateEntity cert1 = CertificateEntity.builder()
                .itemCode("1320")
                .itemName("정보처리기사")
                .largeFieldName("정보기통신")
                .build();

        CertificateEntity cert2 = CertificateEntity.builder()
                .itemCode("0130")
                .itemName("빅데이터분석기사")
                .largeFieldName("정보기통신")
                .build();

        certificateRepository.saveAll(Arrays.asList(cert1, cert2));

        // 2. 시험 일정(Schedule) 데이터 생성 (기준일: 2026-03-20)
        
        // Case 1: 필기만 접수중 (실기는 아직 미정 또는 없음) -> '접수중'에 잡혀야 함
        ScheduleEntity schedule1 = createSchedule(cert1, "2026", 1, 
            LocalDate.of(2026, 3, 15), LocalDate.of(2026, 3, 25), // 필기 접수기간
            null, null, "2026년 정기 기사 1회 (필기 접수중)"); // 실기 접수기간 (null)
            
        // Case 2: 필기는 예전에 마감, 지금은 실기가 접수중! -> '접수중'에 잡혀야 함 (새로 추가된 핵심 로직 검증용)
        ScheduleEntity schedule2 = createSchedule(cert2, "2026", 1, 
            LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 10), // 필기는 1월에 마감됨
            LocalDate.of(2026, 3, 18), LocalDate.of(2026, 3, 28), "2026년 정기 기사 1회 (실기 접수중)");

        // Case 3: 필기와 실기가 겹쳐서 접수중 -> '접수중'에 잡혀야 함
        ScheduleEntity schedule3 = createSchedule(cert1, "2026", 2, 
            LocalDate.of(2026, 3, 10), LocalDate.of(2026, 3, 30), 
            LocalDate.of(2026, 3, 15), LocalDate.of(2026, 3, 25), "2026년 특별 기사 (필/실기 동시접수)");

        // Case 4: 필기는 마감, 실기가 접수 예정 -> '다가오는 시험'에 잡혀야 함
        ScheduleEntity schedule4 = createSchedule(cert2, "2026", 2, 
            LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 10), 
            LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 10), "2026년 정기 기사 2회 (실기 다가옴)");

        // Case 5: 완전 종료된 시험 -> 어디에도 안 잡히고 전체 조회에만 나와야 함
        ScheduleEntity schedule5 = createSchedule(cert1, "2026", 3, 
            LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 5), 
            LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 5), "2026년 정기 기사 3회 (완전 종료됨)");

        // Case 6: 내년 시험 -> 전체 조회(올해~내년)에만 나와야 함
        ScheduleEntity schedule6 = createSchedule(cert2, "2027", 1, 
            LocalDate.of(2027, 3, 15), LocalDate.of(2027, 3, 25), 
            LocalDate.of(2027, 5, 15), LocalDate.of(2027, 5, 25), "2027년 정기 기사 1회");

        scheduleRepository.saveAll(Arrays.asList(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6));

        System.out.println(">>> 🚀 실기시험 포함 더미 데이터 셋업 완료!");
    }

    // 객체 생성을 도와주는 헬퍼 메서드 (실기 관련 필드 추가 적용)
    private ScheduleEntity createSchedule(CertificateEntity cert, String year, int seq, 
                                          LocalDate wRegStart, LocalDate wRegEnd, 
                                          LocalDate pRegStart, LocalDate pRegEnd,
                                          String desc) {
        return ScheduleEntity.builder()
                .certificate(cert)
                .implYear(year)
                .implSeq(seq)
                // 필기 정보
                .writtenRegStart(wRegStart)
                .writtenRegEnd(wRegEnd)
                .writtenExamStart(wRegStart != null ? wRegStart.plusDays(20) : null)
                .writtenExamEnd(wRegEnd != null ? wRegEnd.plusDays(20) : null)
                .writtenPassDate(wRegEnd != null ? wRegEnd.plusDays(30) : null)
                // 실기 정보
                .practicalRegStart(pRegStart)
                .practicalRegEnd(pRegEnd)
                .practicalExamStart(pRegStart != null ? pRegStart.plusDays(20) : null)
                .practicalExamEnd(pRegEnd != null ? pRegEnd.plusDays(20) : null)
                .practicalPassDate(pRegEnd != null ? pRegEnd.plusDays(30) : null)
                // 기타 정보
                .description(desc)
                .officeName("한국산업인력공단 대구본부")
                .examLocation("대구공업대학교")
                .build();
    }
}