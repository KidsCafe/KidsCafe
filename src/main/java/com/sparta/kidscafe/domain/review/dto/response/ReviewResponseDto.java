package com.sparta.kidscafe.domain.review.dto.response;

import com.sparta.kidscafe.domain.review.entity.Review;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record ReviewResponseDto(
    Long id,
    Long userId,
    Long cafeId,
    double star,
    String content,
    Long parentId,
    int depth,
    List<ReviewResponseDto> replies
) {

  public static ReviewResponseDto from(Review review) {
    return new ReviewResponseDto(
        review.getId(),
        review.getUser().getId(),
        review.getCafe().getId(),
        review.getStar(),
        review.getContent(),
        review.getParentReview() != null ? review.getParentReview().getId() : null, // Parent review ID
        review.getDepth(),
        mapReplies(review)
    );
  }

  private static List<ReviewResponseDto> mapReplies(Review review) {
    if (review.getReplies() == null || review.getReplies().isEmpty()) {
      return Collections.emptyList(); // Return an empty list if there are no replies
    }
    return review.getReplies().stream()
        .map(ReviewResponseDto::from)
        .collect(Collectors.toList());
  }
}
