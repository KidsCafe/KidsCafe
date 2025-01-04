package com.sparta.kidscafe.domain.coupon.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.coupon.dto.request.CouponRequestDto;
import com.sparta.kidscafe.domain.coupon.dto.response.CouponResponseDto;
import com.sparta.kidscafe.domain.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
      @Valid @RequestBody CouponRequestDto couponRequestDto) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(couponService.createCoupon(authUser, cafeId, couponRequestDto));
  }

  @PatchMapping("/cafes/{cafeId}/coupons/{couponId}")
  public ResponseEntity<StatusDto> updateCoupon(
      @Auth AuthUser authUser, @PathVariable Long cafeId, @PathVariable Long couponId,
      @Valid @RequestBody CouponRequestDto couponRequestDto) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(couponService.updateCoupon(authUser, cafeId, couponId, couponRequestDto));
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

  @PostMapping("/{couponId}/assign/{userId}")
  public ResponseEntity<StatusDto> assignCouponToUser(@PathVariable Long couponId, @PathVariable Long userId) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(couponService.assignCouponToUser(couponId, userId));
  }

  @PutMapping("/{couponId}/use")
  public ResponseEntity<StatusDto> useCoupon(@Auth AuthUser authUser, @PathVariable Long couponId) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(couponService.useCoupon(authUser, couponId));
  }
}
