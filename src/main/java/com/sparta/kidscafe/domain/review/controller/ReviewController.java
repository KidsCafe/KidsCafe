package com.sparta.kidscafe.domain.review.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.review.dto.request.ReviewCreateRequestDto;
import com.sparta.kidscafe.domain.review.dto.response.ReviewResponseDto;
import com.sparta.kidscafe.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

  private final ReviewService reviewService;

  @PostMapping("/cafes/{cafeId}/reviews")
  public ResponseEntity<StatusDto> createReview(
      @Auth AuthUser authUser,
      @Valid @RequestBody ReviewCreateRequestDto request,
      @PathVariable("cafeId") Long cafeId
  ) {
    StatusDto response = reviewService.createReview(authUser, request, cafeId);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/cafes/{cafeId}/reviews")
  public ResponseEntity<PageResponseDto<ReviewResponseDto>> getReviews(
      @PathVariable("cafeId") Long cafeId,
      @RequestParam(defaultValue = "1", name = "page") @Min(1) int page,
      @RequestParam(defaultValue = "10", name = "size") @Min(1) @Max(50) int size
  ) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(reviewService.getReviews(cafeId, PageRequest.of(Math.max(0, page - 1), size)));
  }

  @GetMapping("/reviews")
  public ResponseEntity<PageResponseDto<ReviewResponseDto>> getMyReviews(
      @Auth AuthUser authUser,
      @RequestParam(defaultValue = "1", name = "page") @Min(1) int page,
      @RequestParam(defaultValue = "10", name = "size") @Min(1) @Max(50) int size
  ) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(reviewService.getMyReviews(authUser, PageRequest.of(Math.max(0, page - 1), size)));
  }

  @PutMapping("/reviews/{reviewId}")
  public ResponseEntity<StatusDto> updateReview(
      @Auth AuthUser authUser,
      @PathVariable("reviewId") Long reviewId,
      @Valid @RequestBody ReviewCreateRequestDto request
  ) {
    StatusDto response = reviewService.updateReview(authUser, reviewId, request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/reviews/{reviewId}")
  public ResponseEntity<Void> deleteReview(
      @Auth AuthUser authUser,
      @PathVariable("reviewId") Long reviewId
  ) {
    reviewService.deleteReview(authUser, reviewId);
    return ResponseEntity.noContent().build();
  }
}
