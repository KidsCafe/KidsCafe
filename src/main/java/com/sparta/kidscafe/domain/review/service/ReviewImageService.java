package com.sparta.kidscafe.domain.review.service;


import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.enums.ImageType;
import com.sparta.kidscafe.common.util.FileStorageUtil;
import com.sparta.kidscafe.domain.review.entity.Review;
import com.sparta.kidscafe.domain.review.entity.ReviewImage;
import com.sparta.kidscafe.domain.review.repository.ReviewImageRepository;
import com.sparta.kidscafe.domain.review.repository.ReviewRepository;
import com.sparta.kidscafe.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.sparta.kidscafe.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewImageService {

  private final ReviewImageRepository reviewImageRepository;
  private final FileStorageUtil fileStorage;
  private final ReviewRepository reviewRepository;

  public StatusDto uploadImage(Long userId, Long reviewId, List<MultipartFile> reviewImages) {
    List<ReviewImage> images = new ArrayList<>();
    String dirPath = fileStorage.makeDirectory(ImageType.REVIEW, userId);
    System.out.println("dirPath = " + dirPath);
    for (MultipartFile image : reviewImages) {
      String imagePath = fileStorage.makeFileName(dirPath, image);
      imagePath = fileStorage.uploadImage(imagePath, image);
      ReviewImage reviewImage = ReviewImage.builder()
          .reviewId(reviewId)
          .imagePath(imagePath)
          .build();
      images.add(reviewImage);
    }
    reviewImageRepository.saveAll(images);

    return StatusDto.builder()
        .status(HttpStatus.CREATED.value())
        .message("리뷰 이미지 등록 성공")
        .build();
  }

  public void deleteImage(AuthUser authUser, Long reviewImageId) {
    ReviewImage reviewImage = reviewImageRepository.findById(reviewImageId)
        .orElseThrow(() -> new BusinessException(IMAGE_NOT_FOUND));
    Review review = reviewRepository.findById(reviewImage.getReviewId())
        .orElseThrow(() -> new BusinessException(REVIEW_NOT_FOUND));
    Long id = authUser.getId();

    if (!id.equals(review.getUser().getId())) {
      throw new BusinessException(FORBIDDEN);
    }

    reviewImage.deleteReviewId();
  }
}