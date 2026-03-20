package com.example.certificate_service.domain.dto;

import com.example.certificate_service.domain.entity.CertificateEntity;
import com.example.certificate_service.domain.entity.ScheduleEntity;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CertificateWithSchedulesDTO {
    // 자격증 기본 정보
    private String itemCode;
    private String itemName;
    private String certTypeName;
    private String seriesName;
    private String largeFieldName;
    private String mediumFieldName;

    // 시험 일정 목록
    private List<ScheduleInfo> schedules;

    @Getter
    @Builder
    public static class ScheduleInfo {
        private String implYear;
        private Integer implSeq;
        private String description;
        private LocalDate writtenRegStart;
        private LocalDate writtenRegEnd;
        private LocalDate writtenExamStart;
        private LocalDate writtenExamEnd;
        private LocalDate writtenPassDate;
        private LocalDate practicalRegStart;
        private LocalDate practicalRegEnd;
        private LocalDate practicalExamStart;
        private LocalDate practicalExamEnd;
        private LocalDate practicalPassDate;
        private String officeName;
        private String examLocation;
    }

    public static CertificateWithSchedulesDTO from(CertificateEntity cert, List<ScheduleEntity> schedules) {
        return CertificateWithSchedulesDTO.builder()
                .itemCode(cert.getItemCode())
                .itemName(cert.getItemName())
                .certTypeName(cert.getCertTypeName())
                .seriesName(cert.getSeriesName())
                .largeFieldName(cert.getLargeFieldName())
                .mediumFieldName(cert.getMediumFieldName())
                .schedules(schedules.stream().map(s -> ScheduleInfo.builder()
                        .implYear(s.getImplYear())
                        .implSeq(s.getImplSeq())
                        .description(s.getDescription())
                        .writtenRegStart(s.getWrittenRegStart())
                        .writtenRegEnd(s.getWrittenRegEnd())
                        .writtenExamStart(s.getWrittenExamStart())
                        .writtenExamEnd(s.getWrittenExamEnd())
                        .writtenPassDate(s.getWrittenPassDate())
                        .practicalRegStart(s.getPracticalRegStart())
                        .practicalRegEnd(s.getPracticalRegEnd())
                        .practicalExamStart(s.getPracticalExamStart())
                        .practicalExamEnd(s.getPracticalExamEnd())
                        .practicalPassDate(s.getPracticalPassDate())
                        .officeName(s.getOfficeName())
                        .examLocation(s.getExamLocation())
                        .build())
                        .collect(Collectors.toList()))
                .build();
    }
}