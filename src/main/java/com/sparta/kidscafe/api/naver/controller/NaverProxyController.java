package com.sparta.kidscafe.api.naver.controller;

import com.sparta.kidscafe.api.naver.response.NaverApiResponse;
import com.sparta.kidscafe.api.naver.service.NaverApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
@RequestMapping("/api/naver")
@Validated
public class NaverProxyController {

  private static final Logger logger = LoggerFactory.getLogger(NaverProxyController.class);

  private final NaverApiService naverApiService;

  public NaverProxyController(NaverApiService naverApiService) {
    this.naverApiService = naverApiService;
  }

  @Operation(summary = "네이버 로컬 검색", description = "네이버 로컬 검색 API를 호출하여 결과를 반환합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "검색 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청"),
      @ApiResponse(responseCode = "500", description = "내부 서버 오류")
  })
  @GetMapping("/search")
  public ResponseEntity<NaverApiResponse> searchLocal(
      @RequestParam @NotBlank @Parameter(description = "검색어") String query,
      @RequestParam(defaultValue = "10") @Min(10) @Max(100) @Parameter(description = "표시할 개수 (10~100)") int display,
      @RequestParam(defaultValue = "1") @Min(1) @Max(1000) @Parameter(description = "시작 위치 (1~1000)") int start,
      @RequestParam(defaultValue = "random") @Parameter(description = "정렬 방식 (random, sim, date)") String sort
  ) {
    logger.info("Received request to search local with query: {}, display: {}, start: {}, sort: {}", query, display, start, sort);
    try {
      // NaverApiService를 통해 검색 결과 가져오기
      NaverApiResponse response = naverApiService.searchLocal(query, display, start, sort);
      logger.info("Successfully retrieved local search results for query: {}", query);
      return ResponseEntity.ok(response); // 검색 결과 반환
    } catch (Exception e) {
      logger.error("Error occurred while searching local: {}", e.getMessage(), e);
      return ResponseEntity.status(500).body(null); // 오류 발생 시 500 응답
    }
  }
}