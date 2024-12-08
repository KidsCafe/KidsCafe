package com.sparta.kidscafe.domain.review.service;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.kidscafe.common.client.S3FileUploader;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.review.dto.request.ReviewCreateRequestDto;
import com.sparta.kidscafe.domain.review.dto.request.ReviewImageRequestDto;
import com.sparta.kidscafe.domain.review.entity.Review;
import com.sparta.kidscafe.domain.review.repository.ReviewImageRepository;
import com.sparta.kidscafe.domain.review.repository.ReviewRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

  @InjectMocks
  private ReviewService reviewService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private CafeRepository cafeRepository;

  @Mock
  private ReviewRepository reviewRepository;

  @Mock
  private ReviewImageRepository reviewImageRepository;

  @Mock
  private S3FileUploader s3FileUploader;

  @Test
  void createReview_Success() {
    // Arrange
    User mockUser = User.builder()
        .id(1L)
        .name("Test User")
        .build();

    Cafe mockCafe = Cafe.builder()
        .id(1L)
        .name("Test Cafe")
        .build();

    ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(5, "Great Cafe!");

    List<MultipartFile> mockFiles = List.of(
        new MockMultipartFile("file1", "file1.jpg", "image/jpeg", "dummy content1".getBytes()),
        new MockMultipartFile("file2", "file2.jpg", "image/jpeg", "dummy content2".getBytes())
    );

    ReviewImageRequestDto imageRequestDto = new ReviewImageRequestDto(mockFiles);

    List<String> uploadedUrls = List.of(
        "https://bucket.s3.ap-northeast-2.amazonaws.com/file1.jpg",
        "https://bucket.s3.ap-northeast-2.amazonaws.com/file2.jpg"
    );

    when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
    when(cafeRepository.findById(mockCafe.getId())).thenReturn(Optional.of(mockCafe));
    when(s3FileUploader.uploadFiles(mockFiles)).thenReturn(uploadedUrls);

    // Act
    StatusDto response = reviewService.createReview(mockUser, requestDto, imageRequestDto, mockCafe.getId());

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    assertEquals("리뷰 등록 성공", response.getMessage());

    verify(reviewRepository, times(1)).save(any(Review.class));
    verify(reviewImageRepository, times(1)).saveAll(anyList());
  }

  @Test
  void createReview_UserNotFound() {
    // Arrange
    Long userId = 1L;
    User mockUser = User.builder().id(userId).build();

    ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(5, "Great Cafe!");
    ReviewImageRequestDto imageRequestDto = new ReviewImageRequestDto(List.of());

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> reviewService.createReview(mockUser, requestDto, imageRequestDto, 1L));

    assertEquals("USER NOT FOUND", exception.getMessage());
    verify(userRepository, times(1)).findById(userId);
  }

  @Test
  void createReview_CafeNotFound() {
    // Arrange
    Long cafeId = 1L;
    User mockUser = User.builder().id(1L).build();

    ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(5, "Great Cafe!");
    ReviewImageRequestDto imageRequestDto = new ReviewImageRequestDto(List.of());

    when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
    when(cafeRepository.findById(cafeId)).thenReturn(Optional.empty());

    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> reviewService.createReview(mockUser, requestDto, imageRequestDto, cafeId));

    assertEquals("CAFE NOT FOUND", exception.getMessage());
    verify(cafeRepository, times(1)).findById(cafeId);
  }
}

