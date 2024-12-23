package com.sparta.kidscafe.api.cafe.controller;

import com.sparta.kidscafe.api.cafe.service.CafeCrawlerService;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeResponseDto;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cafes/crawler")
public class CafeCrawlerController {

  private final CafeCrawlerService cafeCrawlerService;

  public CafeCrawlerController(CafeCrawlerService cafeCrawlerService) {
    this.cafeCrawlerService = cafeCrawlerService;
  }

  @GetMapping("/nationwide")
  public ResponseEntity<String> crawlCafesNationwide() {
    cafeCrawlerService.crawlCafesNationwide();
    return ResponseEntity.ok("전국 키즈카페 데이터를 성공적으로 크롤링했습니다.");
  }

  @GetMapping("/region")
  public ResponseEntity<String> crawlCafesByRegion(@RequestParam String city,
      @RequestParam String district) {
    cafeCrawlerService.crawlCafesByRegion(city, district);
    return ResponseEntity.ok(city + " " + district + "의 키즈카페 데이터를 성공적으로 크롤링했습니다.");
  }

  @GetMapping
  public ResponseEntity<List<CafeResponseDto>> getAllCafes() {
    List<CafeResponseDto> cafes = cafeCrawlerService.getAllCafes();
    return ResponseEntity.ok(cafes);
  }

  @GetMapping("/{cafeId}")
  public ResponseEntity<CafeResponseDto> getCafeDetails(@PathVariable Long cafeId) {
    CafeResponseDto cafeDetails = cafeCrawlerService.getCafeDetails(cafeId);
    return ResponseEntity.ok(cafeDetails);
  }
}