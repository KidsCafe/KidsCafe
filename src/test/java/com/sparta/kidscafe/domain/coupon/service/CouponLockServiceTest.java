package com.sparta.kidscafe.domain.coupon.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import com.sparta.kidscafe.domain.coupon.entity.CouponLock;
import com.sparta.kidscafe.domain.coupon.repository.CouponLockRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CouponLockServiceTest {

  private static final Logger logger = LoggerFactory.getLogger(CouponLockServiceTest.class);

  @InjectMocks
  private CouponLockService couponLockService;

  @Mock
  private CouponLockRepository couponLockRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testConcurrentCouponIssuance() throws InterruptedException, ExecutionException {
    // 쿠폰 ID 설정
    Long couponId = 1L;

    // 초기 쿠폰 데이터 설정
    CouponLock mockCoupon = CouponLock.builder()
        .discountType("FIXED") // 정액 할인
        .discountRate(null)    // 정률 할인 없음
        .discountPrice(1000L)  // 정액 할인 1000원
        .maxQuantity(100L)     // 최대 발급 수량 100
        .issuedQuantity(0L)    // 초기 발급 수량 0
        .active(true)          // 쿠폰 활성 상태
        .build();

    // 쿠폰 저장소 Mock 설정
    when(couponLockRepository.findById(couponId)).thenReturn(Optional.of(mockCoupon));

    // 동시 요청 테스트
    int numberOfThreads = 1000; // 요청 수
    ExecutorService executorService = Executors.newFixedThreadPool(50); // 50개의 스레드 풀 생성
    CountDownLatch latch = new CountDownLatch(numberOfThreads);

    // 요청 결과를 저장할 리스트
    List<Future<String>> results = new ArrayList<>();

    for (int i = 0; i < numberOfThreads; i++) {
      results.add(executorService.submit(() -> {
        try {
          mockCoupon.issueCoupon();
          logger.info("Thread {}: Success", Thread.currentThread().getId());
          return "Success";
        } catch (Exception e) {
          logger.error("Thread {}: Failed - {}", Thread.currentThread().getId(), e.getMessage());
          return "Failed: " + e.getMessage();
        } finally {
          latch.countDown();
        }
      }));
    }

    // 모든 요청이 완료될 때까지 대기
    latch.await();
    executorService.shutdown();

    // 결과 검증
    long successCount = results.stream()
        .filter(future -> {
          try {
            return future.get().equals("Success");
          } catch (Exception e) {
            return false;
          }
        })
        .count();

    logger.info("발급 성공 수량: {}", successCount);
    logger.info("최종 쿠폰 발급 수량: {}/{}", mockCoupon.getIssuedQuantity(), mockCoupon.getMaxQuantity());

    // 최대 발급 수량을 초과하지 않았는지 확인
    assertThat(successCount).isEqualTo(mockCoupon.getMaxQuantity());
    assertThat(mockCoupon.getIssuedQuantity()).isEqualTo(mockCoupon.getMaxQuantity());
  }
}