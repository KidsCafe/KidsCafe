package com.sparta.kidscafe.domain.review.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.enums.ImageType;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.common.util.FileStorageUtil;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.review.entity.Review;
import com.sparta.kidscafe.domain.review.entity.ReviewImage;
import com.sparta.kidscafe.domain.review.repository.ReviewImageRepository;
import com.sparta.kidscafe.domain.review.repository.ReviewRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.dummy.DummyCafe;
import com.sparta.kidscafe.dummy.DummyReview;
import com.sparta.kidscafe.dummy.DummyReviewImage;
import com.sparta.kidscafe.dummy.DummyUser;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

public class ReviewImageServiceTest {

  @InjectMocks
  private ReviewImageService reviewImageService;

  @Mock
  private FileStorageUtil fileUtil;

  @Mock
  private ReviewRepository reviewRepository;

  @Mock
  private ReviewImageRepository reviewImageRepository;

  private String dirPath = "https://sparta.com/mock/images/";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private AuthUser createAuthUser() {
    return new AuthUser(1L, "hong@email.com", RoleType.USER);
  }

  @Test
  @DisplayName("이미지 업로드 성공")
  void uploadReviewImage_Success() throws IOException {
    // given - user
    Long cafeId = 1L;
    Long reviewId = 1L;
    AuthUser authUser = createAuthUser();
    User user = DummyUser.createDummyUser(authUser.getRoleType());
    Cafe cafe = DummyCafe.createDummyCafe(user, cafeId);
    Review review = DummyReview.createDummyReview(reviewId, user, cafe);

    // given - image
    dirPath += authUser.getId() + "/" + ImageType.REVIEW.toString().toLowerCase() + "/";
    String imagePath = dirPath + "image.jpg";
    MultipartFile mockImage = mock(MultipartFile.class);
    List<MultipartFile> images = List.of(mockImage);
    List<ReviewImage> reviewImages = new ArrayList<>();
    ReviewImage reviewImage = DummyReviewImage.createDummyReviewImage(review, imagePath);
    reviewImages.add(reviewImage);

    when(fileUtil.makeDirectory(ImageType.REVIEW, authUser.getId())).thenReturn(dirPath);
    when(fileUtil.makeFileName(dirPath, mockImage)).thenReturn(imagePath);
    doNothing().when(mockImage).transferTo(any(File.class));
    when(reviewImageRepository.saveAll(reviewImages)).thenReturn(reviewImages);

    // when
    StatusDto result = reviewImageService.uploadImage(authUser.getId(), reviewId, images);

    // then
    assertEquals(HttpStatus.CREATED.value(), result.getStatus());
    verify(fileUtil, times(1)).makeDirectory(ImageType.REVIEW, authUser.getId());
    verify(fileUtil, times(1)).makeFileName(dirPath, mockImage);
    verify(fileUtil, times(1)).uploadImage(imagePath, mockImage);
    when(reviewImageRepository.saveAll(reviewImages)).thenReturn(reviewImages);
  }

  @Test
  @DisplayName("이미지 삭제 성공 (soft-delete)")
  void deleteImage_Success() {
    // given - user
    Long cafeId = 1L;
    Long reviewId = 1L;
    AuthUser authUser = createAuthUser();
    User user = DummyUser.createDummyUser(authUser.getId(), authUser.getRoleType());
    Cafe cafe = DummyCafe.createDummyCafe(user, cafeId);
    Review review = DummyReview.createDummyReview(reviewId, user, cafe);

    // given - image
    Long imageId = 1L;
    ReviewImage reviewImage = DummyReviewImage.createDummyReviewImage(imageId, review);
    when(reviewImageRepository.findById(imageId)).thenReturn(Optional.of(reviewImage));
    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

    // when
    reviewImageService.deleteImage(authUser, reviewId);

    // then
    verify(reviewImageRepository).findById(imageId);
  }
}