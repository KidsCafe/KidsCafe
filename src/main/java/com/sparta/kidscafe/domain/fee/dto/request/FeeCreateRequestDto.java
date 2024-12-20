package com.sparta.kidscafe.domain.fee.dto.request;

import com.sparta.kidscafe.common.enums.AgeGroup;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.fee.entity.Fee;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeCreateRequestDto {

  @NotNull(message = "연령대가 비어있습니다. 연령대를 선택해주세요.")
  private AgeGroup ageGroup;

  @Positive(message = "요금는 0원 이상이어야 합니다.")
  private int fee;

  public Fee convertDtoToEntity(Cafe cafe) {
    return Fee.builder()
        .cafe(cafe)
        .ageGroup(ageGroup)
        .fee(fee)
        .build();
  }
}
