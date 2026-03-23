package com.inspire.personal_event.personalEvent.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inspire.personal_event.personalEvent.dao.PersonalEventRepository;
import com.inspire.personal_event.personalEvent.domain.dto.PersonalEventRequestDTO;
import com.inspire.personal_event.personalEvent.domain.dto.PersonalEventResponseDTO;
import com.inspire.personal_event.personalEvent.domain.entity.PersonalEventEntity;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonalEventService {
    
    private final PersonalEventRepository personalEventRepository;

    @Transactional
    public PersonalEventResponseDTO create(PersonalEventRequestDTO request, Long user){
        System.out.println(">>> PersonalEvent service create");

        PersonalEventEntity personalEvent = personalEventRepository.save(request.toEntity(user));
        return PersonalEventResponseDTO.fromEntity(personalEvent);
    }

    @Transactional(readOnly = true)
    public PersonalEventResponseDTO read(Long personalEventId, Long user){
        System.out.println(">>>> PersonalEvent service read");
        return personalEventRepository.findByIdAndUser(personalEventId, user)
                                    .map(PersonalEventResponseDTO::fromEntity)
                                    .orElseThrow(()-> new EntityNotFoundException("권한이 없거나 게시글 없음"));
    }

    @Transactional
    public void delete(Long personalEventId, Long user){
        System.out.println(">>>> PersonalEvent service delete");
        PersonalEventEntity personalEvent = personalEventRepository.findByIdAndUser(personalEventId, user)
                                                                    .orElseThrow(() -> new EntityNotFoundException("권한이 없거나 게시글 없음"));
        personalEventRepository.delete(personalEvent);                                                            
    }

    @Transactional(readOnly = true)
    public List<PersonalEventResponseDTO> list(Long user){
        System.out.println(">>>> PersonalEvent service list");
        return personalEventRepository.findAllByUser(user)
                                    .stream()
                                    .map(PersonalEventResponseDTO::fromEntity)
                                    .toList();
    }

    @Transactional
    public PersonalEventResponseDTO update(Long personalEventId, PersonalEventRequestDTO request, Long user){
        System.out.println(">>>> PersonalEvent service update");
        PersonalEventEntity personalEvent = personalEventRepository.findByIdAndUser(personalEventId, user)
                                                                    .orElseThrow(() -> new EntityNotFoundException("수정 권한이 없거나 게시글 없음"));
                                
        personalEvent.update(request.getTitle(), request.getDate(), request.getType(), request.getDescription());

        return PersonalEventResponseDTO.fromEntity(personalEvent);
    }



}
