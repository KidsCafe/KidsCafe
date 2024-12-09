package com.sparta.kidscafe.domain.review.service;

import static com.sparta.kidscafe.exception.ErrorCode.CAFE_NOT_FOUND;
import static com.sparta.kidscafe.exception.ErrorCode.REVIEW_NOT_FOUND;
import static com.sparta.kidscafe.exception.ErrorCode.USER_NOT_FOUND;

import com.sparta.kidscafe.common.client.S3FileUploader;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.ResponseDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final UserRepository userRepository;
  private final CafeRepository cafeRepository;
  private final S3FileUploader s3FileUploader;
  private final ReviewRepository reviewRepository;
  private final ReviewImageRepository reviewImageRepository;

  public StatusDto createReview(User testUser, ReviewCreateRequestDto request,
      List<MultipartFile> reviewImages, Long cafeId) {

    Long id = testUser.getId();

    // 유저 확인
    User user = userRepository.findById(id)
        .orElseThrow(() -> new BusinessException (USER_NOT_FOUND));

    // 카페 확인
    Cafe cafe = cafeRepository.findById(cafeId)
        .orElseThrow(() -> new BusinessException (CAFE_NOT_FOUND));

    Review newReview = Review.builder()
        .user(user)
        .cafe(cafe)
        .star(request.star())
        .content(request.content())
        .build();

    reviewRepository.save(newReview);

    // 이미지 업로드
    List<String> uploads = s3FileUploader.uploadFiles(reviewImages);

    // 이미지 리스트를 리뷰이미지 객체로 변환
    List<ReviewImage> images = new ArrayList<>();
    for (String upload : uploads) {
      ReviewImage reviewImage = ReviewImage.builder()
          .review(newReview)
          .imagePath(upload)
          .build();
      images.add(reviewImage);
    }
    reviewImageRepository.saveAll(images);

    return StatusDto.builder()
        .status(HttpStatus.CREATED.value())
        .message("리뷰 등록 성공")
        .build();
  }

  public PageResponseDto<ReviewResponseDto> getReviews(User testUser, Long cafeId, Pageable pageable) {
    Long id = testUser.getId();

    // 유저 확인
    User user = userRepository.findById(id)
        .orElseThrow(() -> new BusinessException (USER_NOT_FOUND));

    // 카페 확인
    Cafe cafe = cafeRepository.findById(cafeId)
        .orElseThrow(() -> new BusinessException (CAFE_NOT_FOUND));

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

  public PageResponseDto<ReviewResponseDto> getMyReviews(User testUser, PageRequest of) {
    Long id = testUser.getId();

    // 유저 확인
    User user = userRepository.findById(id)
        .orElseThrow(() -> new BusinessException (USER_NOT_FOUND));

    // 유저가 작성한 리뷰 가져오기
    Page<Review> reviews = reviewRepository.findByReviewId(id);

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
}
