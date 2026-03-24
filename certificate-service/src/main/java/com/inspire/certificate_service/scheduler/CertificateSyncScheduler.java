package com.inspire.certificate_service.scheduler;

import com.inspire.certificate_service.service.CertificateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class CertificateSyncScheduler {

    private final CertificateService certificateService;

    // 매일 새벽 2시에 동기화
    @Scheduled(cron = "0 0 2 * * *")
    public void syncAll() {
        log.info("===== 자격증 데이터 동기화 시작 =====");
        
        // 1. 자격증 종목 먼저 동기화 (Certificate)
        certificateService.syncCertificates();
        
        // 2. 올해 일정 동기화
        String thisYear = String.valueOf(LocalDate.now().getYear());
        certificateService.syncSchedules(thisYear);
        
        log.info("===== 자격증 데이터 동기화 완료 =====");
    }

    @EventListener(ApplicationReadyEvent.class) // 서버가 켜지자마자 실행!
    public void init() {
        log.info("서버 기동 확인: 초기 데이터 동기화를 시작합니다.");
        syncAll(); // 기존에 만들어둔 동기화 메서드 호출
    }

}