package com.inspire.certificate_service.domain.dto;

import java.time.LocalDate;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ExamListResponseDTO {

    private String itemCode;
    private String itemName;
    private String largeFieldName;

    private String type; // WR 또는 PR
    private LocalDate startDate;
    private LocalDate endDate;

    private String description;

}