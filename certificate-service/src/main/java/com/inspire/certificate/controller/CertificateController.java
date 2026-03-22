package com.inspire.certificate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/schedules")
public class CertificateController {

    @GetMapping("")
    public ResponseEntity<List<?>> getSchedules() {

        return null;
    }

    @GetMapping("/{itemCode}")
    public ResponseEntity<?> getSchedule(@PathVariable(name = "itemCode") String itemCode) {

        return null;
    }
}
