package com.sparta.kidscafe.domain.coupon.dto.response;

import com.sparta.kidscafe.domain.coupon.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponResponseDto {

    private Long id;
    private String name;
    private Integer discountRate;
    private boolean used;
    private LocalDateTime validTo;

  public static CouponResponseDto from(Coupon coupon) {
    return CouponResponseDto.builder()
        .id(coupon.getId())
        .name(coupon.getName())
        .discountRate(coupon.getDiscount_rate())
        .used(coupon.isUsed())
        .validTo(coupon.getValidTo())
        .build();
  }
}
