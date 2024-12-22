package com.sparta.kidscafe.domain.coupon.controller;

import com.sparta.kidscafe.domain.coupon.dto.request.CouponCreateRequestDto;
import com.sparta.kidscafe.domain.coupon.entity.Coupon;
import com.sparta.kidscafe.domain.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CouponController {

  private final CouponService couponService;

  @PostMapping("/cafes/{cafeId}/coupons")
  public Coupon createCoupon(@PathVariable Long cafeId, @RequestBody CouponCreateRequestDto couponCreateRequestDto) {
    return null;
  }
}
