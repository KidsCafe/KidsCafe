package com.sparta.kidscafe.domain.pricepolicy.dto.request;

import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PricePolicyUpdateRequestDto {

  @NotNull(message = "Target ID는 필수 입력값입니다.")
  @Positive(message = "Target ID는 양수만 허용됩니다.")
  private Long targetId;

  @NotBlank(message = "정책 이름은 필수 입력값입니다.")
  private String title;

  @NotBlank(message = "DayType은 필수 입력값입니다.")
  @Pattern(regexp = "^(월|화|수|목|금|토|일)(,\\s*(월|화|수|목|금|토|일))*$",
      message = "DayType은 요일을 쉼표로 구분하여 입력해야 합니다. (예: '월, 화, 수')")
  private String dayType;

  @NotNull(message = "Rate는 필수 입력값입니다.")
  @DecimalMin(value = "0.0", inclusive = false, message = "Rate는 0보다 커야 합니다.")
  @DecimalMax(value = "50.0", message = "Rate는 최대 50.0까지 입력 가능합니다.")
  @Digits(integer = 2, fraction = 2, message = "Rate는 정수 2자리 이하, 소수점 2자리 이하만 허용됩니다.")
  private Double rate;

  public void updateEntity(PricePolicy pricePolicy) {
    pricePolicy.updateDetails(targetId, title, dayType, rate);
  }
}
