package com.sparta.kidscafe.domain.coupon.service;

import com.sparta.kidscafe.domain.coupon.dto.request.CouponLockCreateRequestDto;
import com.sparta.kidscafe.domain.coupon.dto.request.CouponLockIssueRequestDto;
import com.sparta.kidscafe.domain.coupon.entity.CouponLock;
import com.sparta.kidscafe.domain.coupon.repository.CouponLockRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponLockService {

  private final CouponLockRepository couponLockRepository;


  public void createCoupon(CouponLockCreateRequestDto requestDto) {
    CouponLock coupon = CouponLock.builder()
        .discountType(requestDto.getDiscountType())
        .discountRate(requestDto.getDiscountRate())
        .discountPrice(requestDto.getDiscountPrice())
        .maxQuantity(requestDto.getMaxQuantity())
        .issuedQuantity(0L) // 초기 발급 수량은 0
        .active(true)       // 기본값 활성화
        .build();
    couponLockRepository.save(coupon);
  }

  // 쿠폰 발급 로직 (낙관적 락 적용)
  @Transactional
  public String issueCoupon(Long couponId) {
    for (int i = 0; i < 3; i++) { // 최대 3번 재시도
      try {
        CouponLock coupon = couponLockRepository.findById(couponId)
            .orElseThrow(() -> new IllegalStateException("쿠폰을 찾을 수 없습니다."));

        coupon.issueCoupon(); // 쿠폰 발급
        couponLockRepository.save(coupon); // 발급 수량 업데이트
        return "쿠폰 발급 성공! 현재 발급 수량: " + coupon.getIssuedQuantity() + "/" + coupon.getMaxQuantity();
      } catch (OptimisticLockException e) {
        // 낙관적 락 충돌 시 재시도
        System.out.println("낙관적 락 충돌 발생, 재시도 중... (" + (i + 1) + "/3)");
        try {
          Thread.sleep(50); // 잠시 대기 후 재시도
        } catch (InterruptedException ex) {
          Thread.currentThread().interrupt();
        }
      }
    }
    throw new IllegalStateException("쿠폰 발급 중 충돌이 발생하여 실패했습니다.");
  }
}
