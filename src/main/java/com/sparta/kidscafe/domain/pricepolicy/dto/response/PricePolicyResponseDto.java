package com.sparta.kidscafe.domain.pricepolicy.dto.response;

import com.sparta.kidscafe.common.enums.TargetType;
import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PricePolicyResponseDto {

  private Long id;                // 정책 ID
  private Long cafeId;            // 카페 ID
  private TargetType targetType;  // 정책 대상 종류
  private Long targetId;          // 대상 ID
  private String title;           // 정책 제목
  private String workDay;         // 적용 요일
  private Double rate;            // 요금 비율
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public static PricePolicyResponseDto from(PricePolicy pricePolicy) {
    return PricePolicyResponseDto.builder()
        .id(pricePolicy.getId())
        .cafeId(pricePolicy.getCafe().getId())
        .targetType(pricePolicy.getTargetType())
        .targetId(pricePolicy.getTargetId())
        .title(pricePolicy.getTitle())
        .rate(pricePolicy.getRate())
        .workDay(pricePolicy.getDayType())
        .createdAt(pricePolicy.getCreatedAt())
        .modifiedAt(pricePolicy.getModifiedAt())
        .build();
  }
}
