package com.sparta.kidscafe.domain.coupon.controller;

import com.sparta.kidscafe.domain.coupon.dto.request.CouponLockCreateRequestDto;
import com.sparta.kidscafe.domain.coupon.entity.CouponLock;
import com.sparta.kidscafe.domain.coupon.service.CouponLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CouponLockController {

  private final CouponLockService couponLockService;

  @PostMapping("/cafes/{cafeId}/coupon}")
  public ResponseEntity<String> createCoupon(@RequestBody CouponLockCreateRequestDto requestDto) {
    couponLockService.createCoupon(requestDto);
    return ResponseEntity.ok("Coupon created successfully");
  }

  @PostMapping("/issue/{id}")
  public ResponseEntity<String> issueCoupon(@PathVariable Long id) {
    try {
      String result = couponLockService.issueCoupon(id);
      return ResponseEntity.ok(result);
    } catch (IllegalStateException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
  }
}
