package com.inspire.certificate_service.domain.dto;

import com.inspire.certificate_service.domain.entity.CertificateEntity;
import com.inspire.certificate_service.domain.entity.ExamEntity;
import lombok.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamDetailResponseDTO {
    // 자격증 기본 정보
    private String itemCode;
    private String itemName;
    private String certTypeName;
    private String seriesName;
    private String largeFieldName;

    // 시험 일정 목록 (회차별로 그룹화됨)
    private List<ScheduleInfo> schedules;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
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
    }

    /**
     * Entity 리스트를 받아 DTO로 변환 (그룹화 로직 포함)
     */
    public static ExamDetailResponseDTO from(CertificateEntity cert, List<ExamEntity> allExamRows) {
        // 1. implYear + implSeq 조합을 키로 사용하여 그룹화
        Map<String, List<ExamEntity>> groupedBySession = allExamRows.stream()
                .collect(Collectors.groupingBy(e -> e.getImplYear() + "-" + e.getImplSeq()));

        // 2. 각 그룹을 ScheduleInfo로 변환
        List<ScheduleInfo> scheduleInfos = groupedBySession.values().stream()
                .map(sessionRows -> {
                    ExamEntity first = sessionRows.get(0);
                    ScheduleInfo.ScheduleInfoBuilder builder = ScheduleInfo.builder()
                            .implYear(first.getImplYear())
                            .implSeq(first.getImplSeq())
                            .description(first.getDescription());

                    for (ExamEntity row : sessionRows) {
                        switch (row.getType()) {
                            case "WR": builder.writtenRegStart(row.getStartDate()).writtenRegEnd(row.getEndDate()); break;
                            case "WE": builder.writtenExamStart(row.getStartDate()).writtenExamEnd(row.getEndDate()); break;
                            case "WP": builder.writtenPassDate(row.getEndDate()); break;
                            case "PR": builder.practicalRegStart(row.getStartDate()).practicalRegEnd(row.getEndDate()); break;
                            case "PE": builder.practicalExamStart(row.getStartDate()).practicalExamEnd(row.getEndDate()); break;
                            case "PD": builder.practicalPassDate(row.getEndDate()); break;
                        }
                    }
                    return builder.build();
                })
                .sorted(Comparator.comparing(ScheduleInfo::getImplYear).thenComparing(ScheduleInfo::getImplSeq))
                .collect(Collectors.toList());

        return ExamDetailResponseDTO.builder()
                .itemCode(cert.getItemCode())
                .itemName(cert.getItemName())
                .certTypeName(cert.getCertTypeName())
                .seriesName(cert.getSeriesName())
                .largeFieldName(cert.getLargeFieldName())
                .schedules(scheduleInfos)
                .build();
    }
}