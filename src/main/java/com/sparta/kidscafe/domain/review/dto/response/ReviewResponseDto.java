package com.sparta.kidscafe.domain.review.dto.response;

import com.sparta.kidscafe.domain.review.entity.Review;

public record ReviewResponseDto(
    Long id,
    Long userId,
    Long cafeId,
    double star,
    String content
) {
  public static ReviewResponseDto from(Review review) {
    return new ReviewResponseDto(
        review.getId(),
        review.getUser().getId(),
        review.getCafe().getId(),
        review.getStar(),
        review.getContent()
    );
  }
}
