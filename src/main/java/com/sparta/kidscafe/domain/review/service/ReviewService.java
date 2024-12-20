package com.sparta.kidscafe.domain.review.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.review.dto.request.ReviewCreateRequestDto;
import com.sparta.kidscafe.domain.review.dto.response.ReviewResponseDto;
import com.sparta.kidscafe.domain.review.entity.Review;
import com.sparta.kidscafe.domain.review.entity.ReviewImage;
import com.sparta.kidscafe.domain.review.repository.ReviewImageRepository;
import com.sparta.kidscafe.domain.review.repository.ReviewRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import com.sparta.kidscafe.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.kidscafe.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

  private final UserRepository userRepository;
  private final CafeRepository cafeRepository;
  private final ReviewRepository reviewRepository;
  private final ReviewImageRepository reviewImageRepository;

  public StatusDto createReview(AuthUser authUser, ReviewCreateRequestDto request, Long cafeId) {
    Long userId = authUser.getId();

    // 유저 검증
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

    // 카페 검증
    Cafe cafe = cafeRepository.findById(cafeId)
            .orElseThrow(() -> new BusinessException(CAFE_NOT_FOUND));

    // 부모 리뷰 확인
    Review parentReview = null;
    if (request.parentId() != null) {
      parentReview = reviewRepository.findById(request.parentId())
              .orElseThrow(() -> new BusinessException(REVIEW_NOT_FOUND));

      if (parentReview.getDepth() >= 3) {
        throw new BusinessException(FORBIDDEN, "더 이상 대댓글을 작성할 수 없습니다.");
      }
    }

    // 리뷰 생성
    Review newReview = Review.builder()
            .user(user)
            .cafe(cafe)
            .star(request.star())
            .content(request.content())
            .parentReview(parentReview)
            .depth(parentReview != null ? parentReview.getDepth() + 1 : 0)
            .build();

    reviewRepository.save(newReview);

    // 이미지 연관 설정
    List<ReviewImage> images = reviewImageRepository.findAllById(request.imageId());
    images.forEach(image -> image.updateReviewImages(newReview.getId()));

    return StatusDto.builder()
            .status(HttpStatus.CREATED.value())
            .message("리뷰 등록 성공")
            .build();
  }

  @Transactional(readOnly = true)
  public PageResponseDto<ReviewResponseDto> getReviews(Long cafeId, Pageable pageable) {
    // 카페 확인
    cafeRepository.findById(cafeId)
            .orElseThrow(() -> new BusinessException(CAFE_NOT_FOUND));

    // 특정 카페의 리뷰 조회
    Page<Review> reviews = reviewRepository.findByCafeIdAndParentReviewIsNullWithReplies(cafeId, pageable);

    Page<ReviewResponseDto> reviewDtos = reviews.map(this::mapToReviewResponseDtoWithReplies);
    return PageResponseDto.success(reviewDtos, HttpStatus.OK, "카페 리뷰 조회 성공");
  }

  @Transactional(readOnly = true)
  public PageResponseDto<ReviewResponseDto> getMyReviews(AuthUser authUser, PageRequest pageable) {
    Long userId = authUser.getId();

    // 유저 확인
    userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

    // 유저가 작성한 리뷰 조회
    Page<Review> reviews = reviewRepository.findAllByUserId(userId, pageable);
    Page<ReviewResponseDto> reviewDtos = reviews.map(ReviewResponseDto::from);

    return PageResponseDto.success(reviewDtos, HttpStatus.OK, "내 리뷰 조회 성공");
  }

  public StatusDto updateReview(AuthUser authUser, Long reviewId, ReviewCreateRequestDto request) {
    // 리뷰 확인
    Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new BusinessException(REVIEW_NOT_FOUND));

    // 사용자 검증
    Long userId = authUser.getId();
    if (!userId.equals(review.getUser().getId())) {
      throw new BusinessException(FORBIDDEN, "리뷰를 수정할 권한이 없습니다.");
    }

    // 부모 리뷰 수정 불가
    if (review.getParentReview() != null && request.parentId() != null) {
      throw new BusinessException(FORBIDDEN, "부모 리뷰는 수정할 수 없습니다.");
    }

    // 리뷰 업데이트
    review.updateReview(request.star(), request.content());

    return StatusDto.builder()
            .status(HttpStatus.OK.value())
            .message("리뷰 수정 성공")
            .build();
  }

  public void deleteReview(AuthUser authUser, Long reviewId) {
    // 리뷰 확인
    Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new BusinessException(REVIEW_NOT_FOUND));

    // 사용자 검증
    Long userId = authUser.getId();
    if (!userId.equals(review.getUser().getId())) {
      throw new BusinessException(FORBIDDEN, "리뷰를 삭제할 권한이 없습니다.");
    }

    // 이미지 리뷰 ID 초기화
    List<ReviewImage> images = reviewImageRepository.findAllByReviewId(reviewId);
    images.forEach(ReviewImage::deleteReviewId);

    // 리뷰 삭제
    reviewRepository.delete(review);
  }

  private ReviewResponseDto mapToReviewResponseDtoWithReplies(Review review) {
    List<ReviewResponseDto> childResponses = review.getReplies().stream()
            .map(this::mapToReviewResponseDtoWithReplies)
            .collect(Collectors.toList());

    return new ReviewResponseDto(
            review.getId(),
            review.getUser().getId(),
            review.getCafe().getId(),
            review.getStar(),
            review.getContent(),
            review.getParentReview() != null ? review.getParentReview().getId() : null,
            review.getDepth(),
            childResponses
    );
  }
}