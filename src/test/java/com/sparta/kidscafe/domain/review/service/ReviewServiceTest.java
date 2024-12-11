package com.sparta.kidscafe.domain.review.service;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.kidscafe.common.client.S3FileUploader;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.review.dto.request.ReviewCreateRequestDto;
import com.sparta.kidscafe.domain.review.dto.response.ReviewResponseDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    // Given
    Long userId = 1L;
    Long cafeId = 100L;
    AuthUser authUser = new AuthUser(userId, "testUser", RoleType.USER);
    ReviewCreateRequestDto request = new ReviewCreateRequestDto(5, "Great cafe!");
    List<MultipartFile> mockFiles = List.of(
        new MockMultipartFile("image1.jpg", new byte[]{1, 2, 3}),
        new MockMultipartFile("image2.jpg", new byte[]{4, 5, 6})
    );

    User mockUser = User.builder().id(userId).build();
    Cafe mockCafe = Cafe.builder().id(cafeId).build();
    List<String> mockUploads = List.of(
        "https://bucket.s3.region.amazonaws.com/image1.jpg",
        "https://bucket.s3.region.amazonaws.com/image2.jpg"
    );

    when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
    when(cafeRepository.findById(cafeId)).thenReturn(Optional.of(mockCafe));
    when(s3FileUploader.uploadFiles(mockFiles)).thenReturn(mockUploads);

    // When
    StatusDto result = reviewService.createReview(authUser, request, mockFiles, cafeId);

    // Then
    assertNotNull(result);
    assertEquals(HttpStatus.CREATED.value(), result.getStatus());
    assertEquals("리뷰 등록 성공", result.getMessage());

    verify(userRepository, times(1)).findById(userId);
    verify(cafeRepository, times(1)).findById(cafeId);
    verify(s3FileUploader, times(1)).uploadFiles(mockFiles);
    verify(reviewRepository, times(1)).save(any(Review.class));
    verify(reviewImageRepository, times(1)).saveAll(anyList());
  }

  @Test
  void getMyReviews_Success() {
    // Given
    Long userId = 1L;
    AuthUser authUser = new AuthUser(userId, "testUser", RoleType.USER);
    User user = User.builder().id(userId).build();
    Cafe cafe = Cafe.builder().id(1L).build();
    PageRequest pageable = PageRequest.of(0, 10);

    User mockUser = User.builder().id(userId).build();
    List<Review> mockReviews = List.of(
        new Review(1L, user, cafe, 5, "My first review"),
        new Review(2L, user, cafe, 4, "Another review")
    );
    Page<Review> reviewPage = new PageImpl<>(mockReviews, pageable, mockReviews.size());

    when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
    when(reviewRepository.findAllByUserId(userId, pageable)).thenReturn(reviewPage);

    // When
    PageResponseDto<ReviewResponseDto> result = reviewService.getMyReviews(authUser, pageable);

    // Then
    assertNotNull(result);
    assertEquals(HttpStatus.OK.value(), result.getStatus());
    assertEquals("리뷰 조회 성공", result.getMessage());
    assertEquals(2, result.getData().size());
  }

  @Test
  void updateReview_Success() {
    // Given
    Long userId = 1L;
    Long reviewId = 100L;
    AuthUser authUser = new AuthUser(userId, "testUser", RoleType.USER);
    ReviewCreateRequestDto request = new ReviewCreateRequestDto(4, "Updated content");

    User mockUser = User.builder().id(userId).build();
    Review mockReview = Review.builder()
        .id(reviewId)
        .user(mockUser)
        .star(5)
        .content("Original content")
        .build();

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(mockReview));

    // When
    StatusDto result = reviewService.updateReview(authUser, reviewId, request);

    // Then
    assertNotNull(result);
    assertEquals(HttpStatus.OK.value(), result.getStatus());
    assertEquals("리뷰 수정완료", result.getMessage());

    verify(reviewRepository, times(1)).findById(reviewId);
    assertEquals(request.star(), mockReview.getStar());
    assertEquals(request.content(), mockReview.getContent());
  }

  @Test
  void deleteReview_Success() {
    // Given
    Long userId = 1L;
    Long reviewId = 100L;
    AuthUser authUser = new AuthUser(userId, "testUser", RoleType.USER);

    User mockUser = User.builder().id(userId).build();
    Review mockReview = Review.builder()
        .id(reviewId)
        .user(mockUser)
        .build();

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(mockReview));

    // When
    reviewService.deleteReview(authUser, reviewId);

    // Then
    verify(reviewRepository, times(1)).findById(reviewId);
    verify(reviewRepository, times(1)).delete(mockReview);
  }
}

