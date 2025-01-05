package com.sparta.kidscafe.domain.coupon.dto.response;

import com.sparta.kidscafe.domain.coupon.entity.Coupon;
import java.time.LocalDate;
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
    private String discountType;
    private boolean used;
    private LocalDate validTo;

  public static CouponResponseDto from(Coupon coupon) {
    return CouponResponseDto.builder()
        .id(coupon.getId())
        .name(coupon.getName())
        .discountType(coupon.getDiscountType())
        .used(coupon.isUsed())
        .validTo(coupon.getValidTo())
        .build();
  }
}
