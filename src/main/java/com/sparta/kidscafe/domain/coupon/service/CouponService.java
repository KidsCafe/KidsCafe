package com.sparta.kidscafe.domain.coupon.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.coupon.dto.request.CouponCreateRequestDto;
import com.sparta.kidscafe.domain.coupon.dto.response.CouponResponseDto;
import com.sparta.kidscafe.domain.coupon.entity.Coupon;
import com.sparta.kidscafe.domain.coupon.repository.CouponRepository;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

  private final CouponRepository couponRepository;
  private final CafeRepository cafeRepository;

  private void validationOwner(AuthUser authUser, Cafe cafe) {
    RoleType roleType = authUser.getRoleType();
    if(roleType == RoleType.ADMIN) {
      return;
    }

    if(roleType == RoleType.USER) {
      throw new BusinessException(ErrorCode.COUPON_TABLE_UNAUTHORIZED);
    }

    if(roleType == RoleType.OWNER) {
      throw new BusinessException(ErrorCode.COUPON_TABLE_OWN_CREATE);
    }
  }

  @Transactional
  public StatusDto createCoupon(AuthUser authUser, Long cafeId, @Valid CouponCreateRequestDto couponCreateRequestDto) {
    Cafe cafe = cafeRepository.findById(cafeId)
            .orElseThrow(() -> new BusinessException(ErrorCode.CAFE_NOT_FOUND));

    validationOwner(authUser, cafe);
    Coupon coupon = couponCreateRequestDto.convertToEntity(cafe);

    couponRepository.save(coupon);

    return StatusDto.builder()
            .status(HttpStatus.OK.value())
            .message(coupon.getName() + "쿠폰 생성")
            .build();
  }



  public ListResponseDto<CouponResponseDto> getCouponByUser;
  public ListResponseDto<CouponResponseDto> getCouponByOwner;
}
