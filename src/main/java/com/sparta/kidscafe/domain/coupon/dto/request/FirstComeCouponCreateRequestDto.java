package com.sparta.kidscafe.domain.coupon.dto.request;

import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.coupon.entity.FirstComeCoupon;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FirstComeCouponCreateRequestDto {

  @NotBlank(message = "쿠폰 이름을 입력해주세요")
  private String name;

  @NotNull(message = "할인 정책을 선택해주세요")
  @Pattern(regexp = "FIXED|PERCENT", message = "할인 정책은 FIXED 또는 PERCENT 중 하나여야 합니다.")
  private String discountType;

  private Long discountRate;

  private Long discountPrice;

  @NotNull(message = "발급할 쿠폰 수량을 입력해주세요")
  private Long maxQuantity;

  public FirstComeCoupon convertToEntity(Cafe cafe){
    if (discountType.equals("FIXED") && discountPrice == null) {
      throw new IllegalArgumentException("정액 할인을 위한 discountPrice가 필요합니다.");
    }
    if (discountType.equals("PERCENT") && discountRate == null) {
      throw new IllegalArgumentException("정률 할인을 위한 discountRate가 필요합니다.");
    }
    return FirstComeCoupon.builder()
        .cafe(cafe)
        .name(name)
        .discountType(discountType)
        .discountRate(discountRate)
        .discountPrice(discountPrice)
        .maxQuantity(maxQuantity)
        .issuedQuantity(0L)
        .active(true)
        .build();
  }
}
