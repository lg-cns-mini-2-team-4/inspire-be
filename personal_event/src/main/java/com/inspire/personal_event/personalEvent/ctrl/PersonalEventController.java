package com.inspire.personal_event.personalEvent.ctrl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inspire.personal_event.personalEvent.domain.dto.PersonalEventRequestDTO;
import com.inspire.personal_event.personalEvent.domain.dto.PersonalEventResponseDTO;
import com.inspire.personal_event.personalEvent.service.PersonalEventService;

import lombok.RequiredArgsConstructor;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/personalEvent")
@RequiredArgsConstructor
public class PersonalEventController {
    
    public final PersonalEventService personalEventService;

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody PersonalEventRequestDTO request,
                                        @RequestHeader("X-User-Id") Long user){
        System.out.println(">>>> PersonalEvent ctrl path : /create"); 
        System.out.println(">>>> params : "+ request); 
        System.out.println(">>>> X-User-Email : "+ user); 

        PersonalEventResponseDTO response = personalEventService.create(request, user);
        System.out.println(">>>> PersonalEvent insert flag : "+response); 

        if(response != null ) {
            return new ResponseEntity(HttpStatus.CREATED); // 201
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST); // 400  
        }
    }

    @GetMapping("/read/{personalEventId}")
    public ResponseEntity<PersonalEventResponseDTO> read( @PathVariable("personalEventId") Long personalEventId,
                                                            @RequestHeader("X-User-Id") Long user) {
        System.out.println(">>>> PersonalEvent ctrl path : /read"); 
        System.out.println(">>>> params personalEventId : "+ personalEventId);

        PersonalEventResponseDTO response = personalEventService.read(personalEventId, user);
        if( response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK); // 200
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }

    @DeleteMapping("/delete/{personalEventId}")
    public ResponseEntity<Void> delete( @PathVariable("personalEventId") Long personalEventId,
                                        @RequestHeader("X-User-Id") Long user){
        System.out.println(">>>> PersonalEvent ctrl path : /delete"); 
        System.out.println(">>>> params personalEventId : "+ personalEventId);

        personalEventService.delete(personalEventId, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @GetMapping("/list")
    public ResponseEntity<List<PersonalEventResponseDTO>> list(@RequestHeader("X-User-Id") Long user) {
        System.out.println(">>>> PersonalEvent ctrl path : /list"); 
        List<PersonalEventResponseDTO> list = personalEventService.list(user);
        if(list.size() != 0) {
            return ResponseEntity.status(HttpStatus.OK).body(list);
        }else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
               
    }

    @PutMapping("/update/{personalEventId}")
    public ResponseEntity<Void> update(@PathVariable("personalEventId") Long personalEventId, 
                                        @RequestBody PersonalEventRequestDTO request,
                                        @RequestHeader("X-User-Id") Long user){
        System.out.println(">>>> PersonalEvent ctrl path : /update");
        System.out.println(">>>> blogId : "+ personalEventId);
        System.out.println(">>>> params  : "+ request); 

        PersonalEventResponseDTO response = personalEventService.update(personalEventId, request, user);

        if(response != null ) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build() ;
        }
    }
    
    
}
