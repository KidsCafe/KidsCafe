package com.sparta.kidscafe.domain.coupon.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.coupon.dto.request.CouponCreateRequestDto;
import com.sparta.kidscafe.domain.coupon.dto.response.CouponResponseDto;
import com.sparta.kidscafe.domain.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
  public ResponseEntity<StatusDto> createCoupone(
      @Auth AuthUser authUser, @PathVariable Long cafeId,
      @Valid @RequestBody CouponCreateRequestDto couponCreateRequestDto) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(couponService.createCoupon(authUser, cafeId, couponCreateRequestDto));
  }

  @GetMapping("/users/coupons")
  public ResponseEntity<ListResponseDto<CouponResponseDto>> getCouponByUser(@Auth AuthUser authUser) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(couponService.getCouponByUser(authUser));
  }

  @GetMapping("/owners/cafes/{cafeId}/coupons")
  public ResponseEntity<ListResponseDto<CouponResponseDto>> getCouponByOwner(@Auth AuthUser authUser, @PathVariable Long cafeId) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(couponService.getCouponByOwner(authUser, cafeId));
  }
}
