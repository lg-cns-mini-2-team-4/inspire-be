package com.example.certificate_service.service;

import com.example.certificate_service.dao.ScheduleRepository;
import com.example.certificate_service.domain.dto.ScheduleActiveResponseDTO;
import com.example.certificate_service.domain.dto.ScheduleCalendarResponseDTO;
import com.example.certificate_service.domain.dto.ScheduleStatusCountResponseDTO;
import com.example.certificate_service.domain.entity.CertificateEntity;
import com.example.certificate_service.domain.entity.ScheduleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    // 1. 현재 접수 중인 시험 3개 조회
    @Transactional(readOnly = true)
    public List<ScheduleActiveResponseDTO> getActiveSchedules() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        Pageable limitThree = PageRequest.of(0, 3);

        List<ScheduleEntity> schedules = scheduleRepository.findActiveSchedules(today, limitThree);

        return schedules.stream().map(schedule -> {
            CertificateEntity cert = schedule.getCertificate();
            return ScheduleActiveResponseDTO.builder()
                    .itemCode(cert.getItemCode())
                    .itemName(cert.getItemName())
                    .largeFieldName(cert.getLargeFieldName())
                    .mediumFieldName(cert.getMediumFieldName())
                    .writtenRegStart(schedule.getWrittenRegStart())
                    .writtenRegEnd(schedule.getWrittenRegEnd())
                    .writtenExamStart(schedule.getWrittenExamStart())
                    .writtenExamEnd(schedule.getWrittenExamEnd())
                    .writtenPassDate(schedule.getWrittenPassDate())
                    .practicalRegStart(schedule.getPracticalRegStart())
                    .practicalRegEnd(schedule.getPracticalRegEnd())
                    .practicalExamStart(schedule.getPracticalExamStart())
                    .practicalExamEnd(schedule.getPracticalExamEnd())
                    .practicalPassDate(schedule.getPracticalPassDate())
                    .description(schedule.getDescription())
                    // .officeName(schedule.getOfficeName())
                    // .examLocation(schedule.getExamLocation())
                    .build();
        }).collect(Collectors.toList());
    }

    // 2.. 다가오는 시험 3개 조회
    @Transactional(readOnly = true)
    public List<ScheduleActiveResponseDTO> getUpcomingSchedules() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        Pageable limitThree = PageRequest.of(0, 3);

        List<ScheduleEntity> schedules = scheduleRepository.findUpcomingSchedules(today, limitThree);

        return schedules.stream().map(schedule -> {
            CertificateEntity cert = schedule.getCertificate();
            return ScheduleActiveResponseDTO.builder()
                    .itemCode(cert.getItemCode())
                    .itemName(cert.getItemName())
                    .largeFieldName(cert.getLargeFieldName())
                    .mediumFieldName(cert.getMediumFieldName())
                    .writtenRegStart(schedule.getWrittenRegStart())
                    .writtenRegEnd(schedule.getWrittenRegEnd())
                    .writtenExamStart(schedule.getWrittenExamStart())
                    .writtenExamEnd(schedule.getWrittenExamEnd())
                    .writtenPassDate(schedule.getWrittenPassDate())
                    .practicalRegStart(schedule.getPracticalRegStart())
                    .practicalRegEnd(schedule.getPracticalRegEnd())
                    .practicalExamStart(schedule.getPracticalExamStart())
                    .practicalExamEnd(schedule.getPracticalExamEnd())
                    .practicalPassDate(schedule.getPracticalPassDate())
                    .description(schedule.getDescription())
                    // .officeName(schedule.getOfficeName())
                    // .examLocation(schedule.getExamLocation())
                    .build();
        }).collect(Collectors.toList());
    }

    // 3. 대시보드 상태별 시험 개수 조회
    @Transactional(readOnly = true)
    public ScheduleStatusCountResponseDTO getScheduleStatusCounts() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        long totalCount = scheduleRepository.count();
        long upcomingCount = scheduleRepository.countUpcomingSchedules(today);
        long activeCount = scheduleRepository.countActiveSchedules(today);
        long completedCount = scheduleRepository.countCompletedSchedules(today);
        return ScheduleStatusCountResponseDTO.builder()
                .totalCount(totalCount)
                .upcomingCount(upcomingCount)
                .activeCount(activeCount)
                .completedCount(completedCount)
                .build();
    }



















    // [기능 3 수정] 특정 연도의 캘린더용 시험 일정 조회
    // [기능 3] 접수 중인 시험 '전체' 조회
    @Transactional(readOnly = true)
    public List<ScheduleActiveResponseDTO> getActiveExamsAll() {
        System.out.println(">>> ScheduleService getActiveExamsAll()");
        // 1. 서울 시간 기준 오늘 날짜 구하기
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        
        // 2. 페이징 제한 없이 모든 데이터를 가져오기 위해 Pageable.unpaged() 사용
        // (참고: 팀원의 Repository 메서드가 Pageable을 인자로 받으므로 그대로 활용 가능)
        List<ScheduleEntity> schedules = scheduleRepository.findActiveSchedules(today, Pageable.unpaged());

        // 3. DTO 변환 후 반환
        return schedules.stream()
                .map(this::convertToActiveDto)
                .collect(Collectors.toList());
    }

    // [기능 4] 전체 시험 조회 (올해 ~ 내년)
    @Transactional(readOnly = true)
    public List<ScheduleActiveResponseDTO> getAllSchedules() {
        System.out.println(">>> ScheduleService getAllScheudels()");
        // 1. 현재 연도와 다음 연도 계산
        int currentYear = LocalDate.now(ZoneId.of("Asia/Seoul")).getYear();
        String thisYear = String.valueOf(currentYear);
        String nextYear = String.valueOf(currentYear + 1);

        // 2. Repository 호출 (올해와 내년 데이터만 조회)
        List<ScheduleEntity> schedules = scheduleRepository.findSchedulesByYears(thisYear, nextYear);

        // 3. Entity -> DTO 변환 및 반환
        return schedules.stream()
            .map(this::convertToActiveDto)
            .collect(Collectors.toList());
    }

    // [기능 5] 특정 연도의 캘린더용 시험 일정 조회
    @Transactional(readOnly = true)
    public List<ScheduleCalendarResponseDTO> getSchedulesByYearForCalendar(String year) {
        
        // 특정 연도의 일정만 조회
        List<ScheduleEntity> schedules = scheduleRepository.findSchedulesByYear(year);

        // Entity -> DTO 매핑
        return schedules.stream().map(schedule -> {
            CertificateEntity cert = schedule.getCertificate();
            return ScheduleCalendarResponseDTO.builder()
                    .itemCode(cert.getItemCode())
                    .itemName(cert.getItemName())
                    .largeFieldName(cert.getLargeFieldName())
                    .mediumFieldName(cert.getMediumFieldName())
                    .writtenRegStart(schedule.getWrittenRegStart())
                    .writtenRegEnd(schedule.getWrittenRegEnd())
                    .writtenExamStart(schedule.getWrittenExamStart())
                    .writtenExamEnd(schedule.getWrittenExamEnd())
                    .writtenPassDate(schedule.getWrittenPassDate())
                    .practicalRegStart(schedule.getPracticalRegStart())
                    .practicalRegEnd(schedule.getPracticalRegEnd())
                    .practicalExamStart(schedule.getPracticalExamStart())
                    .practicalExamEnd(schedule.getPracticalExamEnd())
                    .practicalPassDate(schedule.getPracticalPassDate())
                    .description(schedule.getDescription())
                    .build();
        }).collect(Collectors.toList());
    }

    /**
    * Entity를 ScheduleActiveResponseDTO로 변환하는 공통 빌더 로직
    */
    private ScheduleActiveResponseDTO convertToActiveDto(ScheduleEntity schedule) {
        CertificateEntity cert = schedule.getCertificate();
    
        return ScheduleActiveResponseDTO.builder()
                .itemCode(cert.getItemCode())
                .itemName(cert.getItemName())
                .largeFieldName(cert.getLargeFieldName())
                .mediumFieldName(cert.getMediumFieldName())
                .writtenRegStart(schedule.getWrittenRegStart())
                .writtenRegEnd(schedule.getWrittenRegEnd())
                .writtenExamStart(schedule.getWrittenExamStart())
                .writtenExamEnd(schedule.getWrittenExamEnd())
                .writtenPassDate(schedule.getWrittenPassDate())
                .practicalRegStart(schedule.getPracticalRegStart())
                .practicalRegEnd(schedule.getPracticalRegEnd())
                .practicalExamStart(schedule.getPracticalExamStart())
                .practicalExamEnd(schedule.getPracticalExamEnd())
                .practicalPassDate(schedule.getPracticalPassDate())
                .description(schedule.getDescription())
                // .officeName(schedule.getOfficeName())
                // .examLocation(schedule.getExamLocation())
                .build();
    }
}
