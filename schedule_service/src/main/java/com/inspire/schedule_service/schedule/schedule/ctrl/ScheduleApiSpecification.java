package com.inspire.schedule_service.schedule.schedule.ctrl;

import com.inspire.schedule_service.schedule.schedule.domain.dto.ScheduleRequestDTO;
import com.inspire.schedule_service.schedule.schedule.domain.dto.ScheduleResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleApiSpecification {

    @Operation(summary = "개인 일정 생성")
    ResponseEntity<Void> create(@RequestBody ScheduleRequestDTO request,
                                @RequestHeader("X-User-Id") Long userId);

    @Operation(summary = "개인 전체 일정 조회")
    ResponseEntity<List<ScheduleResponseDTO>> list(@Parameter(name = "type", description = "일정 타입", example = "STUDY")
                                                   @RequestParam(name = "type", required = false) String type,
                                                   @Parameter(name = "startDate", description = "조회 시작일", example = "2026-03-02")
                                                   @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                   @Parameter(name = "endDate", description = "조회 종료일", example = "2026-04-03")
                                                   @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                   @RequestHeader(value = "X-User-Id") Long userId);

    @Operation(summary = "임시")
    ResponseEntity<List<ScheduleResponseDTO>> list2(@RequestHeader(name = "X-User-Id") Long userId);

    @Operation(summary = "일정 조회")
    ResponseEntity<ScheduleResponseDTO> read(@Parameter(name = "id", description = "일정 id", example = "1")
                                             @PathVariable("id") Long id,
                                             @RequestHeader("X-User-Id") Long userId);

    @Operation(summary = "일정 삭제")
    ResponseEntity<ScheduleResponseDTO> delete(@Parameter(name = "id", description = "일정 id", example = "1")
                                               @PathVariable("id") Long id,
                                               @RequestHeader("X-User-Id") Long userId);
}
