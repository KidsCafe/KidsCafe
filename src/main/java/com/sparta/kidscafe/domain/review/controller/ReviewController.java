package com.sparta.kidscafe.domain.review.controller;

import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.review.dto.request.ReviewCreateRequestDto;
import com.sparta.kidscafe.domain.review.dto.request.ReviewImageRequestDto;
import com.sparta.kidscafe.domain.review.dto.response.ReviewResponseDto;
import com.sparta.kidscafe.domain.review.service.ReviewService;
import com.sparta.kidscafe.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

  private final ReviewService reviewService;

  @PostMapping("/cafes/{cafeId}/reviews")
    public ResponseEntity<StatusDto> createReview (
        @Valid @RequestPart ReviewCreateRequestDto request,
        @Valid @RequestPart ReviewImageRequestDto imageRequest,
        @PathVariable Long cafeId
  ) {
    User testUser = User.builder().id(1L).build();
    StatusDto response = reviewService.createReview(testUser, request, imageRequest, cafeId);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
