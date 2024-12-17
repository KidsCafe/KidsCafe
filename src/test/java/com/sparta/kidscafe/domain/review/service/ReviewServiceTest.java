package com.sparta.kidscafe.domain.review.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.enums.RoleType;
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
import java.util.Collections;
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

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private CafeRepository cafeRepository;

  @Mock
  private ReviewRepository reviewRepository;

  @Mock
  private ReviewImageRepository reviewImageRepository;

  @InjectMocks
  private ReviewService reviewService;

  @Test
  void createReview_success() {
    AuthUser authUser = new AuthUser(1L,"test@gmail.com", RoleType.USER);
    ReviewCreateRequestDto request = new ReviewCreateRequestDto(5, "Great place!",List.of());
    Long cafeId = 1L;

    User user = User.builder().id(1L).email("test@gmail.com").build();
    Cafe cafe = Cafe.builder().id(cafeId).build();

    when(userRepository.findById(authUser.getId())).thenReturn(Optional.of(user));
    when(cafeRepository.findById(cafeId)).thenReturn(Optional.of(cafe));

    StatusDto response = reviewService.createReview(authUser, request, cafeId);

    assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    assertEquals("리뷰 등록 성공", response.getMessage());

    verify(reviewRepository, times(1)).save(any(Review.class));
  }

  @Test
  void getReviews_success() {
    Long cafeId = 1L;
    PageRequest pageable = PageRequest.of(0, 10);

    Cafe cafe = Cafe.builder().id(cafeId).build();
    Review review = new Review(1L, new User(1L,"test@gmail.com", RoleType.USER), cafe, 5, "Great!");
    Page<Review> reviewPage = new PageImpl<>(List.of(review));

    when(cafeRepository.findById(cafeId)).thenReturn(Optional.of(cafe));
    when(reviewRepository.findByCafeId(cafeId, pageable)).thenReturn(reviewPage);

    PageResponseDto<ReviewResponseDto> response = reviewService.getReviews(cafeId, pageable);

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals("카페 리뷰 조회 성공", response.getMessage());
  }

  @Test
  void getMyReviews_success() {
    AuthUser authUser = new AuthUser(1L,"test@gmail.com", RoleType.USER);
    PageRequest pageable = PageRequest.of(0, 10);

    Long cafeId = 1L;
    Cafe cafe = Cafe.builder().id(cafeId).build();
    User user = User.builder().id(1L).email("test@gmail.com").build();
    Review review = new Review(1L, new User(1L,"test@gmail.com", RoleType.USER), cafe, 5, "Great!");
    Page<Review> reviewPage = new PageImpl<>(List.of(review));

    when(userRepository.findById(authUser.getId())).thenReturn(Optional.of(user));
    when(reviewRepository.findAllByUserId(authUser.getId(), pageable)).thenReturn(reviewPage);

    PageResponseDto<ReviewResponseDto> response = reviewService.getMyReviews(authUser, pageable);

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals("리뷰 조회 성공", response.getMessage());
  }

  @Test
  void updateReview_success() {
    AuthUser authUser = new AuthUser(1L,"test@gmail.com", RoleType.USER);
    Long reviewId = 1L;
    ReviewCreateRequestDto request = new ReviewCreateRequestDto(5, "Great place!",List.of());

    Long cafeId = 1L;
    Cafe cafe = Cafe.builder().id(cafeId).build();
    Review review = new Review(1L, new User(1L,"test@gmail.com", RoleType.USER), cafe, 5, "Great place!");

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

    StatusDto response = reviewService.updateReview(authUser, reviewId, request);

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals("리뷰 수정완료", response.getMessage());
    assertEquals(5, review.getStar());
    assertEquals("Great place!", review.getContent());
  }

  @Test
  void deleteReview_success() {
    AuthUser authUser = new AuthUser(1L,"test@gmail.com", RoleType.USER);
    Long reviewId = 1L;

    Long cafeId = 1L;
    Cafe cafe = Cafe.builder().id(cafeId).build();
    Review review = new Review(1L, new User(1L,"test@gmail.com", RoleType.USER), cafe, 5, "Great!");
    List<ReviewImage> reviewImages = Collections.singletonList(new ReviewImage(1L, 2L, "image"));

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
    when(reviewImageRepository.findAllByReviewId(reviewId)).thenReturn(reviewImages);

    reviewService.deleteReview(authUser, reviewId);

    verify(reviewImageRepository, times(1)).findAllByReviewId(reviewId);
    verify(reviewRepository, times(1)).delete(review);
  }
}
