package com.sparta.kidscafe.domain.review.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.review.dto.request.ReviewCreateRequestDto;
import com.sparta.kidscafe.domain.review.dto.response.ReviewResponseDto;
import com.sparta.kidscafe.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

  private final ReviewService reviewService;

  @PostMapping( "/cafes/{cafeId}/reviews")
    public ResponseEntity<StatusDto> createReview (
        @Auth AuthUser authUser,
        @Valid @RequestBody ReviewCreateRequestDto request,
        @PathVariable ("cafeId") Long cafeId
  ) {
    StatusDto response = reviewService.createReview(authUser, request, cafeId);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/cafes/{cafeId}/reviews")
    public ResponseEntity<PageResponseDto<ReviewResponseDto>> getReviews (
        @PathVariable ("cafeId") Long cafeId,
        @RequestParam(defaultValue = "0",name = "page") int page,
        @RequestParam(defaultValue = "10",name = "size") int size
  ) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(reviewService.getReviews(cafeId, PageRequest.of(page - 1,size)));
  }

  @GetMapping("/reviews")
    public ResponseEntity<PageResponseDto<ReviewResponseDto>> getMyReviews (
        @Auth AuthUser authUser,
        @RequestParam(defaultValue = "0",name = "page") int page,
        @RequestParam(defaultValue = "10",name = "size") int size
  ) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(reviewService.getMyReviews(authUser, PageRequest.of(page - 1,size)));
  }

  @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<StatusDto> updateReview (
        @Auth AuthUser authUser,
        @PathVariable ("reviewId") Long reviewId,
        @Valid @RequestBody ReviewCreateRequestDto request
  ) {
    StatusDto response = reviewService.updateReview(authUser, reviewId, request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview (@Auth AuthUser authUser, @PathVariable ("reviewId")Long reviewId) {
    reviewService.deleteReview(authUser, reviewId);
    return ResponseEntity.noContent().build();
  }
}
