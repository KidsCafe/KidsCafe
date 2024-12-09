package com.sparta.kidscafe.domain.review.controller;

import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.review.dto.request.ReviewCreateRequestDto;
import com.sparta.kidscafe.domain.review.dto.response.ReviewResponseDto;
import com.sparta.kidscafe.domain.review.entity.Review;
import com.sparta.kidscafe.domain.review.service.ReviewService;
import com.sparta.kidscafe.domain.user.entity.User;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

  @PostMapping(value = "/cafes/{cafeId}/reviews",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatusDto> createReview (
        @Valid @RequestPart ReviewCreateRequestDto request,
        @RequestPart List<MultipartFile> reviewImages,
        @PathVariable Long cafeId
  ) {
    User testUser = User.builder().id(1L).build();
    StatusDto response = reviewService.createReview(testUser, request, reviewImages, cafeId);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/cafes/{cafeId}/review")
    public ResponseEntity<PageResponseDto<ReviewResponseDto>> getReviews (@PathVariable Long cafeId, @RequestParam int page, @RequestParam int size) {
    User testUser = User.builder().id(1L).build();
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(reviewService.getReviews(testUser, cafeId, PageRequest.of(page,size)));
  }
}
