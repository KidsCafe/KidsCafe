package com.sparta.kidscafe.domain.review.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.review.service.ReviewImageService;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewImageController {

  private final ReviewImageService reviewImageService;

  @PostMapping("/reviews/images")
  public ResponseEntity<StatusDto> uploadImage(
          @Auth AuthUser authUser,
          @RequestParam(value = "reviewId", required = false) Optional<Long> reviewId,
          @RequestPart @NotEmpty(message = "이미지 파일은 필수입니다.") List<MultipartFile> reviewImages
  ) {
    // 이미지 리스트가 비어있는지 확인
    if (reviewImages.isEmpty()) {
      return ResponseEntity
              .status(HttpStatus.BAD_REQUEST)
              .body(StatusDto.builder()
                      .status(HttpStatus.BAD_REQUEST.value())
                      .message("이미지 파일을 하나 이상 업로드해주세요.")
                      .build());
    }

    StatusDto response = reviewImageService.uploadImage(
            authUser.getId(),
            reviewId.orElse(null),
            reviewImages
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @DeleteMapping("/reviews/images")
  public ResponseEntity<Void> deleteImage(
          @Auth AuthUser authUser,
          @RequestParam Long reviewImageId
  ) {
    reviewImageService.deleteImage(authUser, reviewImageId);
    return ResponseEntity.noContent().build();
  }
}
