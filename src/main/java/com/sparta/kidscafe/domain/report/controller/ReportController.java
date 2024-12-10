package com.sparta.kidscafe.domain.report.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.report.dto.request.ReportRequestDto;
import com.sparta.kidscafe.domain.report.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReportController {

  private final ReportService reportService;

  @PostMapping("/reviews/{reviewId}/reports")
  public ResponseEntity<StatusDto> createReport (
      @Auth AuthUser authUser,
      @Valid @RequestBody ReportRequestDto request,
      @PathVariable Long reviewId
  ) {
    StatusDto response = reportService.createReport(authUser, request, reviewId);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
