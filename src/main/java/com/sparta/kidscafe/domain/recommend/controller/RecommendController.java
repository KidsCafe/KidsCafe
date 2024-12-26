package com.sparta.kidscafe.domain.recommend.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.recommend.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RecommendController {

  private final RecommendService recommendService;

  @PostMapping("/review/recommend/{reviewId}")
  public ResponseEntity<StatusDto> createRecommend(
      @PathVariable Long reviewId,
      @Auth AuthUser authUser
  ) {
    boolean response = recommendService.createRecommend(reviewId, authUser);

    if (response) {
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(StatusDto.builder().status(HttpStatus.CREATED.value()).message("추천 완료").build());
    } else {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
  }
}
