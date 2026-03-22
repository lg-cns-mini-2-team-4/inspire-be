package com.inspire.schedule_service.schedule.schedule.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inspire.schedule_service.schedule.schedule.dao.ScheduleRepository;
import com.inspire.schedule_service.schedule.schedule.domain.dto.ExamScheduleRequestDTO;
import com.inspire.schedule_service.schedule.schedule.domain.dto.ScheduleRequestDTO;
import com.inspire.schedule_service.schedule.schedule.domain.dto.ScheduleResponseDTO;
import com.inspire.schedule_service.schedule.schedule.domain.entity.EventType;
import com.inspire.schedule_service.schedule.schedule.domain.entity.ScheduleEntity;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    // 1. 개인 일정 수동 생성
    @Transactional
    public ScheduleResponseDTO create(ScheduleRequestDTO request, Long user){
        System.out.println(">>> Schedule service create");

        ScheduleEntity schedule = scheduleRepository.save(request.toEntity(user));
        return ScheduleResponseDTO.fromEntity(schedule);
    }

    // 2. 시험 정보 자동 등록
    @Transactional
    public void registerExamSchedules(ExamScheduleRequestDTO dto, Long user) {
        System.out.println(">>> Schedule service registerExamSchedules");
        List<ScheduleEntity> entities = new ArrayList<>();
        String name = dto.getItemName();

        // 각 날짜별로 일정 생성 (Null이 아닐 때만)
        addIfNotNull(entities, "[" + name + "] 필기 원서접수 시작", dto.getWrittenRegStart(), EventType.DEADLINE, user);
        addIfNotNull(entities, "[" + name + "] 필기 원서접수 종료", dto.getWrittenRegEnd(), EventType.DEADLINE, user);
        addIfNotNull(entities, "[" + name + "] 필기 시험일", dto.getWrittenExamStart(), EventType.EXAM, user);
        addIfNotNull(entities, "[" + name + "] 필기 합격 발표", dto.getWrittenPassDate(), EventType.OTHER, user);
        
        addIfNotNull(entities, "[" + name + "] 실기 원서접수 시작", dto.getPracticalRegStart(), EventType.DEADLINE, user);
        addIfNotNull(entities, "[" + name + "] 실기 원서접수 종료", dto.getPracticalRegEnd(), EventType.DEADLINE, user);
        addIfNotNull(entities, "[" + name + "] 실기 시험일", dto.getPracticalExamStart(), EventType.EXAM, user);
        addIfNotNull(entities, "[" + name + "] 실기 합격 발표", dto.getPracticalPassDate(), EventType.OTHER, user);

        scheduleRepository.saveAll(entities);
    }

    // 3. 일정 상세 조회
    @Transactional(readOnly = true)
    public ScheduleResponseDTO read(Long scheduleId, Long user){
        System.out.println(">>>> Schedule service read");
        return scheduleRepository.findByIdAndUser(scheduleId, user)
                                    .map(ScheduleResponseDTO::fromEntity)
                                    .orElseThrow(() -> new EntityNotFoundException("권한이 없거나 일정 없음"));
    }

    // 4. 일정 삭제
    @Transactional
    public void delete(Long scheduleId, Long user){
        System.out.println(">>>> Schedule service delete");
        ScheduleEntity schedule = scheduleRepository.findByIdAndUser(scheduleId, user)
                                    .orElseThrow(() -> new EntityNotFoundException("권한이 없거나 일정 없음"));
        scheduleRepository.delete(schedule);                                                            
    }

    // 5. 사용자별 전체 일정 목록 (개인+시험)
    @Transactional(readOnly = true)
    public List<ScheduleResponseDTO> list(Long user){
        System.out.println(">>>> Schedule service list");
        return scheduleRepository.findAllByUser(user)
                                    .stream()
                                    .map(ScheduleResponseDTO::fromEntity)
                                    .toList();
    }

    // 6. 일정 수정
    @Transactional
    public ScheduleResponseDTO update(Long scheduleId, ScheduleRequestDTO request, Long user){
        System.out.println(">>>> Schedule service update");
        ScheduleEntity schedule = scheduleRepository.findByIdAndUser(scheduleId, user)
                                    .orElseThrow(() -> new EntityNotFoundException("수정 권한이 없거나 일정 없음"));
                                
        schedule.update(request.getTitle(), request.getDate(), request.getType(), request.getDescription());

        return ScheduleResponseDTO.fromEntity(schedule);
    }


    private void addIfNotNull(List<ScheduleEntity> list, String title, LocalDate date, EventType type, Long user) {
        if (date != null) {
            // Builder를 사용하여 DTO의 파편화된 데이터를 엔티티로 변환
            ScheduleEntity schedule = ScheduleEntity.builder()
                    .title(title)
                    .date(date)
                    .type(type)
                    .user(user)
                    .description("자격증 서비스에서 자동 등록된 일정입니다.")
                    .build();
            
            list.add(schedule); // 생성된 엔티티를 리스트에 추가
        }
    }
}
