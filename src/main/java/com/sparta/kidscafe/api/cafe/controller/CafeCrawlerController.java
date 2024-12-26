package com.sparta.kidscafe.api.cafe.controller;

import com.sparta.kidscafe.api.cafe.service.CafeCrawlerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cafe-crawler")
@Validated
public class CafeCrawlerController {

  private static final Logger logger = LoggerFactory.getLogger(CafeCrawlerController.class);
  private final CafeCrawlerService cafeCrawlerService;

  public CafeCrawlerController(CafeCrawlerService cafeCrawlerService) {
    this.cafeCrawlerService = cafeCrawlerService;
  }

  @Operation(summary = "크롤링 시작", description = "특정 도시와 구의 키즈카페 데이터를 네이버 API를 통해 크롤링합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "크롤링 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청"),
      @ApiResponse(responseCode = "500", description = "내부 서버 오류")
  })
  @GetMapping("/crawl")
  public ResponseEntity<String> crawlCafesByRegion(
      @RequestParam @NotBlank String city,
      @RequestParam @NotBlank String district
  ) {
    logger.info("Received request to crawl cafes for city: {}, district: {}", city, district);
    try {
      cafeCrawlerService.crawlCafesByRegion(city, district);
      return ResponseEntity.ok("Crawling completed successfully for city: " + city + ", district: " + district);
    } catch (Exception e) {
      logger.error("Error occurred during cafe crawling for city: {}, district: {}: {}", city, district, e.getMessage(), e);
      return ResponseEntity.status(500).body("Failed to crawl cafes for city: " + city + ", district: " + district);
    }
  }
}