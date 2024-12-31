package com.sparta.kidscafe.domain.coupon.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import com.sparta.kidscafe.domain.coupon.entity.CouponLock;
import com.sparta.kidscafe.domain.coupon.repository.CouponLockRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
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
  void testConcurrentCouponIssuance() throws InterruptedException {
    Long couponId = 1L;

    CouponLock mockCoupon = CouponLock.builder()
        .discountType("FIXED")
        .discountRate(null)
        .discountPrice(1000L)
        .maxQuantity(50L)
        .issuedQuantity(0L)
        .active(true)
        .build();

    when(couponLockRepository.findById(couponId)).thenReturn(Optional.of(mockCoupon));

    int numberOfThreads = 150;
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