package com.sparta.kidscafe.domain.coupon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponLockResponseDto {

  private String message;
  private Long issuedQuantity;
  private Long maxQuantity;

}
