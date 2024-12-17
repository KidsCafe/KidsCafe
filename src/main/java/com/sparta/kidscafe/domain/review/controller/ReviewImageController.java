package com.sparta.kidscafe.domain.review.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.review.service.ReviewImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewImageController {

  private final ReviewImageService reviewImageService;

  @PostMapping("/reviews/images")
    public ResponseEntity<StatusDto> uploadImage (
      @Auth AuthUser authUser,
      @RequestParam(value = "reviewId", required = false) String reviewId,
      @RequestPart List<MultipartFile> reviewImages
  ) {
    Long parseId = StringUtils.hasText(reviewId) ? Long.parseLong(reviewId) : null;
    StatusDto response = reviewImageService.uploadImage(authUser.getId(), parseId, reviewImages);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @DeleteMapping("/reviews/images")
    public ResponseEntity<Void> deleteImage (
      @Auth AuthUser authUser,
      @RequestParam Long reviewImageId
  ) {
      reviewImageService.deleteImage(authUser, reviewImageId);
      return  ResponseEntity.noContent().build();
  }
}
