package com.sparta.kidscafe.domain.fee.dto.response;

import java.time.LocalDateTime;

import com.sparta.kidscafe.common.enums.AgeGroup;
import com.sparta.kidscafe.domain.fee.entity.Fee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FeeResponseDto {
  private Long id;
  private AgeGroup ageGroup;
  private int fee;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public static FeeResponseDto from(Fee fee) {
    return FeeResponseDto.builder()
        .id(fee.getId())
        .ageGroup(fee.getAgeGroup())
        .fee(fee.getFee())
        .createdAt(fee.getCreatedAt())
        .modifiedAt(fee.getModifiedAt())
        .build();
  }
}
