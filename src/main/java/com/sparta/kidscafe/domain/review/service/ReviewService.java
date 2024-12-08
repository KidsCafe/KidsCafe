package com.sparta.kidscafe.domain.review.service;

import com.sparta.kidscafe.common.client.S3FileUploader;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.review.dto.request.ReviewCreateRequestDto;
import com.sparta.kidscafe.domain.review.entity.Review;
import com.sparta.kidscafe.domain.review.entity.ReviewImage;
import com.sparta.kidscafe.domain.review.repository.ReviewImageRepository;
import com.sparta.kidscafe.domain.review.repository.ReviewRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
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
      @RequestPart List<MultipartFile> reviewImages, Long cafeId) {

    Long id = testUser.getId();

    // 유저 확인
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("USER NOT FOUND"));

    // 카페 확인
    Cafe cafe = cafeRepository.findById(cafeId)
        .orElseThrow(() -> new IllegalArgumentException("CAFE NOT FOUND"));

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
}
