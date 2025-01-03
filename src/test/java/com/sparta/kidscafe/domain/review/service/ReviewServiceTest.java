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
    AuthUser authUser = new AuthUser(1L, "test@gmail.com", RoleType.USER);
    ReviewCreateRequestDto request = new ReviewCreateRequestDto(5.0, "Great place!", null,
        List.of(1L, 2L));
    Long cafeId = 1L;

    User user = User.builder().id(1L).email("test@gmail.com").build();
    Cafe cafe = Cafe.builder().id(cafeId).name("Test Cafe").build();

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

    Cafe cafe = Cafe.builder().id(cafeId).name("Test Cafe").build();
    User user = User.builder().id(1L).email("test@gmail.com").role(RoleType.USER).build();

    // 대댓글 추가
    Review childReview = Review.builder()
        .id(2L)
        .user(user)
        .cafe(cafe)
        .star(4.0)
        .content("Child review")
        .replies(Collections.emptyList()) // 대댓글도 빈 리스트로 초기화
        .build();

    // 부모 리뷰 설정
    Review review = Review.builder()
        .id(1L)
        .user(user)
        .cafe(cafe)
        .star(5.0)
        .content("Great!")
        .replies(List.of(childReview)) // 대댓글 추가
        .build();

    Page<Review> reviewPage = new PageImpl<>(List.of(review), pageable, 1);

    // Mock 설정
    when(cafeRepository.findById(cafeId)).thenReturn(Optional.of(cafe));
    when(
        reviewRepository.findByCafeIdAndParentReviewIsNullWithReplies(cafeId, pageable)).thenReturn(
        reviewPage);

    // 테스트 실행
    PageResponseDto<ReviewResponseDto> response = reviewService.getReviews(cafeId, pageable);

    // 검증
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals("카페 리뷰 조회 성공", response.getMessage());
    assertEquals(1, response.getData().size());

    ReviewResponseDto responseDto = response.getData().get(0);
    assertEquals(1L, responseDto.id()); // 리뷰 ID 확인
    assertEquals(5.0, responseDto.star()); // 별점 확인
    assertEquals("Great!", responseDto.content()); // 내용 확인
    assertEquals(cafeId, responseDto.cafeId()); // 카페 ID 확인
    assertEquals(user.getId(), responseDto.userId()); // 사용자 ID 확인

    // 대댓글 검증
    ReviewResponseDto childResponseDto = responseDto.replies().get(0);
    assertEquals(2L, childResponseDto.id());
    assertEquals(4.0, childResponseDto.star());
    assertEquals("Child review", childResponseDto.content());
  }


  @Test
  void getMyReviews_success() {
    AuthUser authUser = new AuthUser(1L, "test@gmail.com", RoleType.USER);
    PageRequest pageable = PageRequest.of(0, 10);

    User user = User.builder().id(1L).email("test@gmail.com").build();
    Review review = Review.builder()
        .id(1L)
        .user(user)
        .cafe(Cafe.builder().id(1L).name("Test Cafe").build())
        .star(5.0)
        .content("Great!")
        .build();

    Page<Review> reviewPage = new PageImpl<>(List.of(review));

    when(userRepository.findById(authUser.getId())).thenReturn(Optional.of(user));
    when(reviewRepository.findAllByUserId(authUser.getId(), pageable)).thenReturn(reviewPage);

    PageResponseDto<ReviewResponseDto> response = reviewService.getMyReviews(authUser, pageable);

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals("리뷰 조회 성공", response.getMessage());
  }

  @Test
  void updateReview_success() {
    AuthUser authUser = new AuthUser(1L, "test@gmail.com", RoleType.USER);
    Long reviewId = 1L;
    ReviewCreateRequestDto request = new ReviewCreateRequestDto(4.0, "Updated review!", null,
        List.of(3L));

    Review review = Review.builder()
        .id(reviewId)
        .user(User.builder().id(1L).email("test@gmail.com").role(RoleType.USER).build())
        .cafe(Cafe.builder().id(1L).name("Test Cafe").build())
        .star(5.0)
        .content("Great!")
        .build();

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

    StatusDto response = reviewService.updateReview(authUser, reviewId, request);

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals("리뷰 수정 완료", response.getMessage());
    assertEquals(4.0, review.getStar());
    assertEquals("Updated review!", review.getContent());
  }

  @Test
  void deleteReview_success() {
    AuthUser authUser = new AuthUser(1L, "test@gmail.com", RoleType.USER);
    Long reviewId = 1L;

    Review review = Review.builder()
        .id(reviewId)
        .user(User.builder().id(1L).email("test@gmail.com").role(RoleType.USER).build())
        .cafe(Cafe.builder().id(1L).name("Test Cafe").build())
        .star(5.0)
        .content("Great!")
        .build();

    List<ReviewImage> reviewImages = Collections.singletonList(new ReviewImage(1L, 2L, "image"));

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
    when(reviewImageRepository.findAllByReviewId(reviewId)).thenReturn(reviewImages);

    reviewService.deleteReview(authUser, reviewId);

    verify(reviewImageRepository, times(1)).findAllByReviewId(reviewId);
    verify(reviewRepository, times(1)).delete(review);
  }
}