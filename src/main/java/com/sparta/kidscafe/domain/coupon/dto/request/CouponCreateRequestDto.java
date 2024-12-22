package com.sparta.kidscafe.domain.coupon.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponCreateRequestDto {

    @NotBlank(message = "쿠폰 이름을 입력해주세요.")
    private String name;

    @NotNull(message = "할인율을 선택해주세요.")
    private Integer discountRate;

    @Future(message = "유효 기간은 현재일로부터 시작입니다.")
    private LocalDateTime validTo;
}
