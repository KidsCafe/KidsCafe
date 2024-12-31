package com.sparta.kidscafe.domain.coupon.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CouponLockCreateRequestDto {

  private String discountType;
  private Long discountRate;
  private Long discountPrice;
  private Long maxQuantity;
}
