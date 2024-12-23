package com.sparta.kidscafe.api.cafe.controller;

import com.sparta.kidscafe.api.cafe.service.CafeCrawlerService;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cafes")
public class CafeCrawlerController {

  private final CafeCrawlerService cafeCrawlerService;

  public CafeCrawlerController(CafeCrawlerService cafeCrawlerService) {
    this.cafeCrawlerService = cafeCrawlerService;
  }

  /**
   * 부산시의 모든 구에 대해 키즈카페 데이터를 크롤링하고 저장합니다.
   */
  @GetMapping("/crawl")
  public ResponseEntity<String> crawlAllDistricts() {
    cafeCrawlerService.crawlCafesByDistrict();
    return ResponseEntity.ok("부산시의 모든 구에 대한 키즈카페 데이터를 성공적으로 크롤링했습니다.");
  }

  /**
   * 특정 구에 대한 키즈카페 데이터를 크롤링하고 저장합니다.
   *
   * @param district 구 이름
   * @return 성공 메시지
   */
  @GetMapping("/crawl/district")
  public ResponseEntity<String> crawlByDistrict(@RequestParam String district) {
    cafeCrawlerService.crawlCafesByDistrict(district);
    return ResponseEntity.ok(district + "의 키즈카페 데이터를 성공적으로 크롤링했습니다.");
  }

  /**
   * 저장된 모든 키즈카페 데이터를 조회합니다.
   *
   * @return 저장된 카페 데이터 목록
   */
  @GetMapping
  public ResponseEntity<List<CafeResponseDto>> getAllCafes() {
    List<CafeResponseDto> cafes = cafeCrawlerService.getAllCafes();
    return ResponseEntity.ok(cafes);
  }

  /**
   * 특정 카페 정보를 조회합니다.
   *
   * @param cafeId 카페 ID
   * @return 카페 상세 정보
   */
  @GetMapping("/{cafeId}")
  public ResponseEntity<CafeResponseDto> getCafeDetails(@PathVariable Long cafeId) {
    CafeResponseDto cafeDetails = cafeCrawlerService.getCafeDetails(cafeId);
    return ResponseEntity.ok(cafeDetails);
  }
}