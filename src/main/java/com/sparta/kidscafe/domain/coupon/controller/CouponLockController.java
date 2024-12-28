package com.sparta.kidscafe.domain.coupon.controller;

import com.sparta.kidscafe.domain.coupon.dto.request.CouponLockIssueRequestDto;
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
@RequestMapping("/api/coupons")
public class CouponLockController {

  private final CouponLockService couponLockService;

  @PostMapping("/issue/{id}")
  public ResponseEntity<String> issueCoupon(@PathVariable Long id) {
    String result = couponLockService.issueCoupon(id);
    return ResponseEntity.ok(result);
  }
}
