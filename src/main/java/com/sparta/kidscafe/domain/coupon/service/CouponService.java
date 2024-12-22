package com.sparta.kidscafe.domain.coupon.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.coupon.dto.request.CouponCreateRequestDto;
import com.sparta.kidscafe.domain.coupon.dto.response.CouponResponseDto;
import com.sparta.kidscafe.domain.coupon.repository.CouponRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

  private final CouponRepository couponRepository;

  public StatusDto createCoupon(AuthUser authUser, Long cafeId, @Valid CouponCreateRequestDto couponCreateRequestDto) {
    return null;
  }
  public ListResponseDto<CouponResponseDto> getCouponByUser;
  public ListResponseDto<CouponResponseDto> getCouponByOwner;
}
