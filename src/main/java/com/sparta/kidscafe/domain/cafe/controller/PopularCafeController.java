package com.sparta.kidscafe.domain.cafe.controller;

import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeViewCountResponseDto;
import com.sparta.kidscafe.domain.cafe.service.PopularCafeService;
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
public class PopularCafeController {

  private final PopularCafeService popularCafeService;

  // DB에서 지역별 인기 카페 조회
  @GetMapping("/cafes/popular/db")
  public ResponseEntity<ListResponseDto<CafeViewCountResponseDto>> getTopCafesFromDB(
      @RequestParam String region) {
    ListResponseDto<CafeViewCountResponseDto> responseDto = popularCafeService.getTopCafesFromDB(
        region);
    return ResponseEntity.ok(responseDto);
  }

  // Redis에서 지역별 인기 카페 조회
  @GetMapping("/cafes/popular/redis")
  public ResponseEntity<ListResponseDto<CafeViewCountResponseDto>> getPopularCafesFromRedis(
      @RequestParam String region) {
    ListResponseDto<CafeViewCountResponseDto> responseDto = popularCafeService.getTopCafesFromRedis(
        region);
    return ResponseEntity
        .status(responseDto.getStatus())
        .body(responseDto);
  }

  // Redis 캐시 초기화
  @PostMapping("/popular/redis/update")
  public ResponseEntity<Void> updateCafeViewsInRedis(
      @RequestParam String region) {
    popularCafeService.updateCafeViewsInRedis(region);
    return ResponseEntity.noContent().build();
  }

}
