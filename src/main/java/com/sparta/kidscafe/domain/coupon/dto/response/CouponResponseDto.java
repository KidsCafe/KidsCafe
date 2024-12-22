package com.sparta.kidscafe.domain.coupon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponseDto {

    private Long id;
    private String name;
    private Integer discountRate;
    private boolean used;
    private LocalDateTime validTo;
}
