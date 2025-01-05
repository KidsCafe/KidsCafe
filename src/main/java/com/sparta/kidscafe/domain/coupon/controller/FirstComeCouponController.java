package com.sparta.kidscafe.domain.coupon.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.coupon.dto.request.FirstComeCouponCreateRequestDto;
import com.sparta.kidscafe.domain.coupon.service.FirstComeCouponService;
import jakarta.validation.Valid;
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
public class FirstComeCouponController {

  private final FirstComeCouponService firstComeCouponService;

  @PostMapping("/cafes/{cafeId}/first-come")
  public ResponseEntity<StatusDto> createCoupon(
      @Auth AuthUser authUser, @PathVariable Long cafeId,
      @Valid @RequestBody FirstComeCouponCreateRequestDto firstComeCouponCreateRequestDto) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(firstComeCouponService.createCoupon(authUser, cafeId, firstComeCouponCreateRequestDto));
  }

  @PostMapping("/coupons/{couponId}/issue")
  public ResponseEntity<StatusDto> issueCoupon(
      @Auth AuthUser authUser, @PathVariable Long couponId) {
   return ResponseEntity
       .status(HttpStatus.OK)
       .body(firstComeCouponService.issueCoupon(authUser, couponId));
  }
}
