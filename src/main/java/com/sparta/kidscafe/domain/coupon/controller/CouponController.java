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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                .status(HttpStatus.OK)
                .body(couponService.createCoupon(authUser, cafeId, couponCreateRequestDto));
    }

    @GetMapping("/users/coupons")
    public ResponseEntity<ListResponseDto<CouponResponseDto>> getCouponByUser(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(couponService.getCouponByUser);
    }

    @GetMapping("/owners/cafes/{cafeId}/coupons")
    public ResponseEntity<ListResponseDto<CouponResponseDto>> getCouponByOwner(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(couponService.getCouponByOwner);
    }
}
