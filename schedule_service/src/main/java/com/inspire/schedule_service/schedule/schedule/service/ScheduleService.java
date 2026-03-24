package com.inspire.schedule_service.schedule.schedule.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inspire.schedule_service.schedule.schedule.dao.ScheduleRepository;
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

    // 1. 개인 일정 생성
    @Transactional
    public void create(List<ScheduleRequestDTO> schedules, Long userId){

        schedules.forEach(schedule -> {
            ScheduleEntity entity = schedule.toEntity(userId);
            scheduleRepository.save(entity);});
    }

    // 2. schedule 조회
    @Transactional
    public List<ScheduleResponseDTO> getMySchedules(Long userId) {
        List<ScheduleEntity> entities = scheduleRepository.findAllByUserId(userId);

        return entities.stream()
                .map(ScheduleResponseDTO::fromEntity)
                .toList();
    }
    

    // 3. 일정 상세 조회
    @Transactional(readOnly = true)
    public ScheduleResponseDTO read(Long scheduleId, Long user){
        System.out.println(">>>> Schedule service read");
        return scheduleRepository.findByIdAndUserId(scheduleId, user)
                                    .map(ScheduleResponseDTO::fromEntity)
                                    .orElseThrow(() -> new EntityNotFoundException("권한이 없거나 일정 없음"));
    }

    // 4. 일정 삭제
    @Transactional
    public void delete(Long scheduleId, Long user){
        System.out.println(">>>> Schedule service delete");
        ScheduleEntity schedule = scheduleRepository.findByIdAndUserId(scheduleId, user)
                                    .orElseThrow(() -> new EntityNotFoundException("권한이 없거나 일정 없음"));
        scheduleRepository.delete(schedule);                                                            
    }

    // 5. 자격증 즐겨찾기 취소 시 일괄삭제
    @Transactional
    public void deleteAllByFavorite(Long userId, String itemCode) {
        scheduleRepository.deleteByUserIdAndRefId(userId, itemCode);
    }
}

