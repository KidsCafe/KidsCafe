package com.sparta.kidscafe.domain.coupon.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.coupon.dto.request.FirstComeCouponCreateRequestDto;
import com.sparta.kidscafe.domain.coupon.entity.FirstComeCoupon;
import com.sparta.kidscafe.domain.coupon.repository.FirstComeCouponRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FirstComeCouponServiceTest {

  private static final Logger logger = LoggerFactory.getLogger(FirstComeCouponServiceTest.class);

  @InjectMocks
  private FirstComeCouponService firstComeCouponService;

  @Mock
  private FirstComeCouponRepository firstComeCouponRepository;

  @Mock
  private CafeRepository cafeRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("쿠폰 생성: OWNER 권한-본인가게-성공")
  void createCoupon_owner_success(){
    //given
    AuthUser authUser = new AuthUser(1L, "owner@test.com", RoleType.OWNER);
    Cafe cafe = Cafe.builder()
        .id(1L)
        .user(authUser.toUser())
        .build();

    FirstComeCouponCreateRequestDto requestDto = new FirstComeCouponCreateRequestDto("Coupon",
        "FIXED", null, 1000L, 100L);

    when(cafeRepository.findById(1L)).thenReturn(Optional.of(cafe));

    // when
    firstComeCouponService.createCoupon(authUser, 1L, requestDto);

    // then
    verify(cafeRepository, times(1)).findById(1L);
    verify(firstComeCouponRepository, times(1)).save(any(FirstComeCoupon.class));
  }

  @Test
  @DisplayName("쿠폰 생성: OWNER 권한-본인 가게 X-실패")
  void createCoupon_owner_fail(){
    // given
    AuthUser authUser = new AuthUser(1L, "owner@test.com", RoleType.OWNER);
    Cafe otherCafe = Cafe.builder()
        .id(2L)
        .user(mock(User.class))
        .build();

    FirstComeCouponCreateRequestDto requestDto = new FirstComeCouponCreateRequestDto("Coupon",
        "FIXED", null, 1000L, 100L);

    when(cafeRepository.findById(2L)).thenReturn(Optional.of(otherCafe));

    // when // then
    assertThatThrownBy(() -> firstComeCouponService.createCoupon(authUser, 2L, requestDto))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(ErrorCode.COUPON_TABLE_OWN_CREATE.getMessage());

    verify(cafeRepository, times(1)).findById(2L);
    verify(firstComeCouponRepository, never()).save(any());
  }

  @Test
  @DisplayName("쿠폰 생성: USER 권한-실패")
  void createCoupon_user_fail(){
    // given
    AuthUser authUser = new AuthUser(1L, "user@test.com", RoleType.USER);
    Cafe mockCafe = mock(Cafe.class);

    when(cafeRepository.findById(1L)).thenReturn(Optional.of(mockCafe));

    FirstComeCouponCreateRequestDto requestDto = new FirstComeCouponCreateRequestDto("Coupon",
        "FIXED", null, 1000L, 100L);

    // when // then
    assertThatThrownBy(() -> firstComeCouponService.createCoupon(authUser, 1L, requestDto))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(ErrorCode.COUPON_TABLE_UNAUTHORIZED.getMessage());
  }

  @Test
  @DisplayName("선착순 쿠폰: 동시성 문제 발생-낙관적 락 테스트")
  void testConcurrentCouponIssuance() throws InterruptedException {
    Long couponId = 1L;

    FirstComeCoupon mockCoupon = FirstComeCoupon.builder()
        .discountType("FIXED")
        .discountRate(null)
        .discountPrice(1000L)
        .maxQuantity(50L)
        .issuedQuantity(0L)
        .active(true)
        .build();

    when(firstComeCouponRepository.findById(couponId)).thenReturn(Optional.of(mockCoupon));

    int numberOfThreads = 100;
    ExecutorService executorService = Executors.newFixedThreadPool(50);
    CountDownLatch latch = new CountDownLatch(numberOfThreads * 2);
    BlockingQueue<String> logQueue = new ArrayBlockingQueue<>(numberOfThreads * 2);

    for (int i = 0; i < numberOfThreads; i++) {
      executorService.submit(() -> {
        for (int j = 0; j < 2; j++) {
          try {
            synchronized (mockCoupon) {
              mockCoupon.issueCoupon();
            }
            logQueue.put("Thread " + Thread.currentThread().getId() + " Request " + (j + 1) + ": Success");
          } catch (Exception e) {
            try {
              logQueue.put("Thread " + Thread.currentThread().getId() + " Request " + (j + 1) + ": Failed - " + e.getMessage());
            } catch (InterruptedException ex) {
              Thread.currentThread().interrupt();
            }
          } finally {
            latch.countDown();
          }
        }
      });
    }

    latch.await();
    executorService.shutdown();

    // **로그를 큐에서 하나씩 출력**
    while (!logQueue.isEmpty()) {
      logger.info(logQueue.poll());
    }

    long successCount = mockCoupon.getIssuedQuantity();
    logger.info("발급 성공 수량: {}", successCount);
    logger.info("최종 쿠폰 발급 수량: {}/{}", mockCoupon.getIssuedQuantity(), mockCoupon.getMaxQuantity());

    assertThat(successCount).isEqualTo(mockCoupon.getMaxQuantity());
    assertThat(mockCoupon.getIssuedQuantity()).isEqualTo(mockCoupon.getMaxQuantity());
  }

}