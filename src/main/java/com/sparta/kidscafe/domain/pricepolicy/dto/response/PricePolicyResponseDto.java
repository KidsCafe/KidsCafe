package com.sparta.kidscafe.domain.pricepolicy.dto.response;

import com.sparta.kidscafe.common.enums.TargetType;
import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PricePolicyResponseDto {
    private Long id;           // 정책 ID
    private String title;      // 정책 제목
    private Long cafeId;       // 카페 ID
    private TargetType targetType; // 정책 대상 종류
    private Long targetId;     // 대상 ID
    private String targetName; // 대상 이름
    private String dayType;    // 적용 요일
    private Double rate;       // 요금 비율

    public static PricePolicyResponseDto fromEntity(PricePolicy policy, String targetName) {
        return PricePolicyResponseDto.builder()
                .id(policy.getId())
                .title(policy.getTitle())
                .cafeId(policy.getCafe().getId())
                .targetType(policy.getTargetType())
                .targetId(policy.getTargetId())
                .targetName(targetName)
                .dayType(policy.getDayType())
                .rate(policy.getRate())
                .build();
    }
}
