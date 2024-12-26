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
import com.sparta.kidscafe.dummy.DummyReviewImage;
import com.sparta.kidscafe.dummy.DummyUser;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class) // Mockito 초기화 자동 설정
public class ReviewImageServiceTest {

  @InjectMocks
  private ReviewImageService reviewImageService;
  @Mock
  private FileStorageUtil fileUtil;
  @Mock
  private ReviewRepository reviewRepository;
  @Mock
  private ReviewImageRepository reviewImageRepository;

  private AuthUser createAuthUser() {
    return new AuthUser(1L, "hong@email.com", RoleType.USER);
  }

  @Test
  @DisplayName("이미지 업로드 성공")
  void uploadReviewImage_Success() throws IOException {
    // given - user
    String dirPath = "https://sparta.com/mock/images"; // 지역 변수로 변경
    Long reviewId = 1L;
    AuthUser authUser = createAuthUser();
    User user = DummyUser.createDummyUser(authUser.getRoleType());
    Cafe cafe = DummyCafe.createDummyCafe(user, reviewId);

    // 빌더를 이용한 Review 객체 생성
    Review review = Review.builder()
        .id(reviewId)
        .user(user)
        .cafe(cafe)
        .star(5.0)
        .content("Great!")
        .depth(0)
        .build();

    // given - image
    String imagePath = dirPath + "image.jpg";
    MultipartFile mockImage = mock(MultipartFile.class);
    List<MultipartFile> images = List.of(mockImage);
    ReviewImage reviewImage = DummyReviewImage.createDummyReviewImage(review, imagePath);

    when(fileUtil.makeDirectory(ImageType.REVIEW, authUser.getId())).thenReturn(dirPath);
    when(fileUtil.makeFileName(dirPath, mockImage)).thenReturn(imagePath);
    doNothing().when(mockImage).transferTo(any(File.class));
    when(reviewImageRepository.save(any(ReviewImage.class))).thenReturn(reviewImage);

    // when
    StatusDto result = reviewImageService.uploadImage(authUser.getId(), reviewId, images);

    // then
    assertEquals(HttpStatus.CREATED.value(), result.getStatus());
    verify(fileUtil, times(1)).makeDirectory(ImageType.REVIEW, authUser.getId());
    verify(fileUtil, times(1)).makeFileName(dirPath, mockImage);
    verify(fileUtil, times(1)).uploadImage(imagePath, mockImage);
    verify(reviewImageRepository, times(1)).save(any());
  }

  @Test
  @DisplayName("이미지 삭제 성공 (soft-delete)")
  void deleteImage_Success() {
    // given - user
    Long reviewId = 1L;
    AuthUser authUser = createAuthUser();
    User user = DummyUser.createDummyUser(authUser.getRoleType());
    Cafe cafe = DummyCafe.createDummyCafe(user, reviewId);

    // 빌더를 이용한 Review 객체 생성
    Review review = Review.builder()
        .id(reviewId)
        .user(user)
        .cafe(cafe)
        .star(5.0)
        .content("Great!")
        .depth(0)
        .build();

    // given - image
    Long imageId = 1L;
    ReviewImage reviewImage = DummyReviewImage.createDummyReviewImage(imageId, review);

    when(reviewImageRepository.findById(imageId)).thenReturn(Optional.of(reviewImage));
    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

    // when
    reviewImageService.deleteImage(authUser, reviewId);

    // then
    verify(reviewImageRepository).findById(imageId);
    assertNull(reviewImage.getReviewId());
  }
}