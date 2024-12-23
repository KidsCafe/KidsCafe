package com.sparta.kidscafe.domain.fee.dto.request;

import com.sparta.kidscafe.common.enums.AgeGroup;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FeeUpdateRequestDto {

  @NotNull(message = "연령대가 비어있습니다.")
  private AgeGroup ageGroup;

  @Positive(message = "방 입장료는 0원 이상입니다.")
  private int fee;
}
