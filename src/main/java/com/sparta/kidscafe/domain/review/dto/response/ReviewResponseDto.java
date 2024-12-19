package com.sparta.kidscafe.domain.review.dto.response;

import com.sparta.kidscafe.domain.review.entity.Review;
import java.util.List;

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
        review.getParentReview() != null ? review.getParentReview().getId() : null, // 부모 ID
        review.getDepth(),
        review.getReplies() != null
            ? review.getReplies().stream().map(ReviewResponseDto::from).toList() // 대댓글 변환
            : java.util.List.of()
    );
  }
}
