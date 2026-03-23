package com.inspire.personal_event.personalEvent.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inspire.personal_event.personalEvent.domain.entity.PersonalEventEntity;


@Repository
public interface PersonalEventRepository extends JpaRepository<PersonalEventEntity, Long> {
    // 특정 유저의 모든 이벤트 조회
    List<PersonalEventEntity> findAllByUser(Long user);
    
    // 특정 유저의 특정 이벤트 단건 조회 (권한 검증 포함)
    Optional<PersonalEventEntity> findByIdAndUser(Long id, Long user);
}
