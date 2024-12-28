package com.sparta.kidscafe.domain.coupon.service;

import com.sparta.kidscafe.domain.coupon.dto.request.CouponLockIssueRequestDto;
import com.sparta.kidscafe.domain.coupon.entity.CouponLock;
import com.sparta.kidscafe.domain.coupon.repository.CouponLockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponLockService {

  private final CouponLockRepository couponLockRepository;

  @Transactional
  public String issueCoupon(Long couponId) {
    CouponLock coupon = couponLockRepository.findByIdWithLock(couponId)
        .orElseThrow(() -> new IllegalStateException("쿠폰을 찾을 수 없습니다."));

    coupon.issueCoupon(); // 쿠폰 발급
    couponLockRepository.save(coupon); // 발급 수량 업데이트

    return "쿠폰 발급 성공! 현재 발급 수량: " + coupon.getIssuedQuantity() + "/" + coupon.getMaxQuantity();
  }
}
