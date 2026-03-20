package com.example.certificate_service.service;

import com.example.certificate_service.dao.ScheduleRepository;
import com.example.certificate_service.domain.dto.ScheduleActiveResponseDTO;
import com.example.certificate_service.domain.dto.ScheduleCalendarResponseDTO;
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

    @Transactional(readOnly = true)
    public List<ScheduleActiveResponseDTO> getActiveSchedules() {
        // K8S 파드(Pod)의 UTC 타임존 설정을 대비하여 명시적으로 서울 시간 객체 생성
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        
        // 3개의 데이터만 제한적으로 가져오기 위해 PageRequest 생성
        Pageable limitThree = PageRequest.of(0, 3);
        
        List<ScheduleEntity> schedules = scheduleRepository.findActiveSchedules(today, limitThree);

        // Entity -> DTO 매핑하여 반환
        return schedules.stream().map(schedule -> {
            CertificateEntity cert = schedule.getCertificate();
            return ScheduleActiveResponseDTO.builder()
                    .itemCode(cert.getItemCode())
                    .itemName(cert.getItemName())
                    .largeFieldName(cert.getLargeFieldName())
                    .writtenRegStart(schedule.getWrittenRegStart())
                    .writtenRegEnd(schedule.getWrittenRegEnd())
                    .writtenExamStart(schedule.getWrittenExamStart())
                    .writtenExamEnd(schedule.getWrittenExamEnd())
                    .officeName(schedule.getOfficeName())
                    .examLocation(schedule.getExamLocation())
                    .build();
        }).collect(Collectors.toList());
    }

    // [기능 2] 다가오는 시험 3개 조회
    @Transactional(readOnly = true)
    public List<ScheduleActiveResponseDTO> getUpcomingSchedules() {
        // KST 타임존 적용
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        
        // 3개 제한
        Pageable limitThree = PageRequest.of(0, 3);
        
        // Repository 호출 (가장 가까운 미래 일정 3개)
        List<ScheduleEntity> schedules = scheduleRepository.findUpcomingSchedules(today, limitThree);

        // 기존에 만들어둔 DTO 재사용하여 반환
        return schedules.stream().map(schedule -> {
            CertificateEntity cert = schedule.getCertificate();
            return ScheduleActiveResponseDTO.builder()
                    .itemCode(cert.getItemCode())
                    .itemName(cert.getItemName())
                    .largeFieldName(cert.getLargeFieldName())
                    .writtenRegStart(schedule.getWrittenRegStart())
                    .writtenRegEnd(schedule.getWrittenRegEnd())
                    .writtenExamStart(schedule.getWrittenExamStart())
                    .writtenExamEnd(schedule.getWrittenExamEnd())
                    .officeName(schedule.getOfficeName())
                    .examLocation(schedule.getExamLocation())
                    .build();
        }).collect(Collectors.toList());
    }

// [기능 3 수정] 특정 연도의 캘린더용 시험 일정 조회
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
                    .writtenRegStart(schedule.getWrittenRegStart())
                    .writtenRegEnd(schedule.getWrittenRegEnd())
                    .writtenExamStart(schedule.getWrittenExamStart())
                    .writtenExamEnd(schedule.getWrittenExamEnd())
                    .writtenPassDate(schedule.getWrittenPassDate())
                    .practicalExamStart(schedule.getPracticalExamStart())
                    .practicalExamEnd(schedule.getPracticalExamEnd())
                    .build();
        }).collect(Collectors.toList());
    }
}
