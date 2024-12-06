package com.sparta.kidscafe.domain.pricepolicy.dto.request;

import com.sparta.kidscafe.common.enums.TargetType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricePolicyCreateRequestDto {
  @NotNull(message = "정책 종류가 비어있습니다.")
  private TargetType target;
  private Long targetId;

  @NotBlank(message = "정책 이름을 입력해주세요.")
  private String title;
  private String dayType;

  @DecimalMin(value = "0.0", message = "최소값은 0.0 이상이어야 합니다.")
  @DecimalMax(value = "50.0", message = "최대값은 50.0 이하여야 합니다.")
  private double rate;

  public PricePolicy convertDtoToEntity(Cafe cafe) {
    return PricePolicy.builder()
        .cafe(cafe)
        .targetType(target)
        .targetId(targetId)
        .title(title)
        .dayType(dayType)
        .rate(rate)
        .build();
  }
}
