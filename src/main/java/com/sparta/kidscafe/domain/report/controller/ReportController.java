package com.sparta.kidscafe.domain.report.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.report.dto.request.ReportRequestDto;
import com.sparta.kidscafe.domain.report.dto.request.ReportUpdateDto;
import com.sparta.kidscafe.domain.report.dto.response.ReportResponseDto;
import com.sparta.kidscafe.domain.report.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReportController {

  private final ReportService reportService;

  @PostMapping("/reviews/{reviewId}/reports")
  public ResponseEntity<StatusDto> createReport(
      @Auth AuthUser authUser,
      @Valid @RequestBody ReportRequestDto request,
      @PathVariable("reviewId") Long reviewId
  ) {
    StatusDto response = reportService.createReport(authUser, request, reviewId);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/owners/reports")
  public ResponseEntity<PageResponseDto<ReportResponseDto>> getMyReports(
      @RequestParam(defaultValue = "0", name = "page") int page,
      @RequestParam(defaultValue = "10", name = "size") int size,
      @Auth AuthUser authUser
  ) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(reportService.getMyReports(authUser, PageRequest.of(page - 1, size)));
  }

  @GetMapping("/admin/reports")
  public ResponseEntity<PageResponseDto<ReportResponseDto>> getReports(
      @RequestParam(defaultValue = "0", name = "page") int page,
      @RequestParam(defaultValue = "10", name = "size") int size
  ) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(reportService.getReports(PageRequest.of(page - 1, size)));
  }

  @PatchMapping("/reports/{reportId}")
  public ResponseEntity<StatusDto> updateReport(
      @Valid @RequestBody ReportUpdateDto request,
      @PathVariable("reportId") Long reportId
  ) {
    StatusDto response = reportService.updateReport(request, reportId);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
