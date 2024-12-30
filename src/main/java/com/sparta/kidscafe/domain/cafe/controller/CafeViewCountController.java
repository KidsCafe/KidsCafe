package com.sparta.kidscafe.domain.cafe.controller;

import com.sparta.kidscafe.common.dto.ResponseDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeViewCountResponseDto;
import com.sparta.kidscafe.domain.cafe.service.CafeViewCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CafeViewCountController {

  private final CafeViewCountService cafeViewCountService;

  // 조회수 증가 API
  @PostMapping("/cafes/{cafeId}/view")
  public ResponseEntity<Integer> incrementView(@PathVariable Long cafeId) {
    int updatedViewCount = cafeViewCountService.incrementViewCount(cafeId);
    return ResponseEntity.ok(updatedViewCount);
  }

  // 조회수 조회
  @GetMapping("/cafes/{cafeId}/view")
  public ResponseEntity<ResponseDto<CafeViewCountResponseDto>> getCafeViewCount(
      @PathVariable Long cafeId) {
    ResponseDto<CafeViewCountResponseDto> responseDto = cafeViewCountService.getViewCount(cafeId);
    return ResponseEntity
        .status(responseDto.getStatus())
        .body(responseDto);
  }
}
