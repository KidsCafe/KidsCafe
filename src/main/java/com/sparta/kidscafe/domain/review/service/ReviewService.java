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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

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

    Long id = authUser.getId();

    // 유저 확인
    User user = userRepository.findById(id).orElseThrow(() -> new BusinessException (USER_NOT_FOUND));

    // 카페 확인
    Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new BusinessException (CAFE_NOT_FOUND));

    Review newReview = Review.builder()
        .user(user)
        .cafe(cafe)
        .star(request.star())
        .content(request.content())
        .build();

    reviewRepository.save(newReview);

    List<ReviewImage> images = reviewImageRepository.findAllById(request.imageId());

    for (ReviewImage reviewImage : images) {
      reviewImage.updateReviewImages(newReview.getId());
    }

    return StatusDto.builder()
        .status(HttpStatus.CREATED.value())
        .message("리뷰 등록 성공")
        .build();
  }

  @Transactional(readOnly = true)
  public PageResponseDto<ReviewResponseDto> getReviews(Long cafeId, Pageable pageable) {
    // 카페 확인
    cafeRepository.findById(cafeId).orElseThrow(() -> new BusinessException (CAFE_NOT_FOUND));

    // 특정 리뷰 조회
    Page<Review> reviews = reviewRepository.findByCafeId(cafeId, pageable);

    // 리뷰 목록 변환
    Page<ReviewResponseDto> reviewDtos = reviews.map(review -> new ReviewResponseDto(
            review.getId(),
            review.getUser().getId(),
            review.getCafe().getId(),
            review.getStar(),
            review.getContent()
        ));

    return PageResponseDto.success(reviewDtos,HttpStatus.OK, "카페 리뷰 조회 성공");
  }

  @Transactional(readOnly = true)
  public PageResponseDto<ReviewResponseDto> getMyReviews(AuthUser authUser, PageRequest pageable) {
    Long id = authUser.getId();

    // 유저 확인
    userRepository.findById(id).orElseThrow(() -> new BusinessException (USER_NOT_FOUND));

    // 유저가 작성한 리뷰 가져오기
    Page<Review> reviews = reviewRepository.findAllByUserId(id, pageable);

    // 리뷰 목록 변환
    Page<ReviewResponseDto> reviewDtos = reviews.map(review -> new ReviewResponseDto(
        review.getId(),
        review.getUser().getId(),
        review.getCafe().getId(),
        review.getStar(),
        review.getContent()
    ));

    return PageResponseDto.success(reviewDtos,HttpStatus.OK, "리뷰 조회 성공");
  }

  public StatusDto updateReview(AuthUser authUser, Long reviewId, ReviewCreateRequestDto request) {
    // 리뷰 가져오기
    Optional<Review> review = reviewRepository.findById(reviewId);

    // 리뷰 사용자 확인
    Long id = authUser.getId();

    if (!id.equals(review.get().getUser().getId())) {
      throw new BusinessException (FORBIDDEN);
    }

    // 리뷰 입히기
    review.ifPresent(value -> value.UpdateReview(request.star(), request.content()));

    return StatusDto.builder()
        .status(HttpStatus.OK.value())
        .message("리뷰 수정완료")
        .build();
  }

  public void deleteReview(AuthUser authUser, Long reviewId) {
    // 리뷰확인
    Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new BusinessException(REVIEW_NOT_FOUND));

    // 리뷰 사용자 확인
    Long id = authUser.getId();

    if (!id.equals(review.getUser().getId())) {
      throw new BusinessException(FORBIDDEN);
    }

    List<ReviewImage> allByReviewId = reviewImageRepository.findAllByReviewId(reviewId);

    for(ReviewImage reviewImage : allByReviewId) {
      reviewImage.deleteReviewId();
    }

    reviewRepository.delete(review);
  }
}
