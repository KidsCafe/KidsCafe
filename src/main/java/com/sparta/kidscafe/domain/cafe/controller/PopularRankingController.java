package com.sparta.kidscafe.domain.cafe.controller;

import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeRankingResponseDto;
import com.sparta.kidscafe.domain.cafe.service.CafeRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PopularRankingController {

  private final CafeRankingService cafeRankingService;

  @GetMapping("/cafes/popular/ranking")
  public ResponseEntity<ListResponseDto<CafeRankingResponseDto>> getTopN(
      @RequestParam String region,
      @RequestParam(defaultValue = "10") int n) {
    ListResponseDto<CafeRankingResponseDto> responseDto = cafeRankingService.getTopN(region, n);
    return ResponseEntity
        .status(responseDto.getStatus())
        .body(responseDto);
  }

  @PostMapping("/cafes/popular/ranking/update")
  public ResponseEntity<StatusDto> refreshTopN(
      @RequestParam String region,
      @RequestParam(defaultValue = "10") int n) {
    StatusDto statusDto = cafeRankingService.refreshTopN(region, n);
    return ResponseEntity
        .status(statusDto.getStatus())
        .body(statusDto);
  }
}
