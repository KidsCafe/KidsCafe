package com.sparta.kidscafe.domain.review.service;

import static com.sparta.kidscafe.exception.ErrorCode.CAFE_NOT_FOUND;
import static com.sparta.kidscafe.exception.ErrorCode.USER_NOT_FOUND;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.kidscafe.common.client.S3FileUploader;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.review.dto.request.ReviewCreateRequestDto;
import com.sparta.kidscafe.domain.review.dto.response.ReviewResponseDto;
import com.sparta.kidscafe.domain.review.entity.Review;
import com.sparta.kidscafe.domain.review.repository.ReviewImageRepository;
import com.sparta.kidscafe.domain.review.repository.ReviewRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import com.sparta.kidscafe.exception.BusinessException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    List<MultipartFile> reviewImages = List.of();

    List<String> uploadedUrls = List.of(
        "https://bucket.s3.ap-northeast-2.amazonaws.com/file1.jpg",
        "https://bucket.s3.ap-northeast-2.amazonaws.com/file2.jpg"
    );

    when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
    when(cafeRepository.findById(mockCafe.getId())).thenReturn(Optional.of(mockCafe));
    when(s3FileUploader.uploadFiles(any())).thenReturn(uploadedUrls);

    // Act
    StatusDto response = reviewService.createReview(mockUser, requestDto, reviewImages, mockCafe.getId());

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
    List<MultipartFile> reviewImages = List.of();

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // Act & Assert
    BusinessException exception = assertThrows(BusinessException.class,
        () -> reviewService.createReview(mockUser, requestDto, reviewImages, 1L));

    assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());
    verify(userRepository, times(1)).findById(userId);
  }

  @Test
  void createReview_CafeNotFound() {
    // Arrange
    Long cafeId = 1L;
    User mockUser = User.builder().id(1L).build();

    ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(5, "Great Cafe!");
    List<MultipartFile> reviewImages = List.of();

    when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
    when(cafeRepository.findById(cafeId)).thenReturn(Optional.empty());

    // Act & Assert
    BusinessException exception = assertThrows(BusinessException.class,
        () -> reviewService.createReview(mockUser, requestDto, reviewImages, cafeId));

    assertEquals("카페를 찾을 수 없습니다.", exception.getMessage());
    verify(cafeRepository, times(1)).findById(cafeId);
  }

  @Test
  void getReviews_Success() {
    // Given
    Long testUserId = 1L;
    Long cafeId = 100L;
    User testUser = User.builder().id(testUserId).build();

    User mockUser = User.builder().id(testUserId).build();
    Cafe mockCafe = Cafe.builder().id(cafeId).build();

    List<Review> mockReviews = List.of(
        new Review(1L, mockUser, mockCafe, 5, "Great place!"),
        new Review(2L, mockUser, mockCafe, 4, "Nice coffee!")
    );

    Pageable pageable = PageRequest.of(0, 10);
    Page<Review> reviewPage = new PageImpl<>(mockReviews, pageable, mockReviews.size());

    // Mocking
    when(userRepository.findById(testUserId)).thenReturn(Optional.of(mockUser));
    when(cafeRepository.findById(cafeId)).thenReturn(Optional.of(mockCafe));
    when(reviewRepository.findByCafeId(cafeId, pageable)).thenReturn(reviewPage);

    // When
    PageResponseDto<ReviewResponseDto> result = reviewService.getReviews(testUser, cafeId, pageable);

    // Then
    assertNotNull(result);
    assertEquals(HttpStatus.OK.value(), result.getStatus());
    assertEquals("카페 리뷰 조회 성공", result.getMessage());
    assertEquals(2, result.getData().size());

    ReviewResponseDto review1 = result.getData().get(0);
    assertEquals(1L, review1.id());
    assertEquals(5, review1.star());
    assertEquals("Great place!", review1.content());

    ReviewResponseDto review2 = result.getData().get(1);
    assertEquals(2L, review2.id());
    assertEquals(4, review2.star());
    assertEquals("Nice coffee!", review2.content());

    verify(userRepository, times(1)).findById(testUserId);
    verify(cafeRepository, times(1)).findById(cafeId);
    verify(reviewRepository, times(1)).findByCafeId(cafeId, pageable);
  }

  @Test
  void getReviews_UserNotFound() {
    // Given
    Long testUserId = 1L;
    Long cafeId = 100L;
    User testUser = User.builder().id(testUserId).build();

    when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

    // When / Then
    BusinessException exception = assertThrows(
        BusinessException.class,
        () -> reviewService.getReviews(testUser, cafeId, PageRequest.of(0, 10))
    );

    assertEquals(USER_NOT_FOUND, exception.getErrorCode());
    verify(userRepository, times(1)).findById(testUserId);
    verify(cafeRepository, never()).findById(any());
    verify(reviewRepository, never()).findByCafeId(any(), any());
  }

  @Test
  void getReviews_CafeNotFound() {
    // Given
    Long testUserId = 1L;
    Long cafeId = 100L;
    User testUser = User.builder().id(testUserId).build();

    User mockUser = User.builder().id(testUserId).build();

    when(userRepository.findById(testUserId)).thenReturn(Optional.of(mockUser));
    when(cafeRepository.findById(cafeId)).thenReturn(Optional.empty());

    // When / Then
    BusinessException exception = assertThrows(
        BusinessException.class,
        () -> reviewService.getReviews(testUser, cafeId, PageRequest.of(0, 10))
    );

    assertEquals(CAFE_NOT_FOUND, exception.getErrorCode());
    verify(userRepository, times(1)).findById(testUserId);
    verify(cafeRepository, times(1)).findById(cafeId);
    verify(reviewRepository, never()).findByCafeId(any(), any());
  }
}

