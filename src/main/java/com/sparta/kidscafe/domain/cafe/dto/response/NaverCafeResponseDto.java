package com.sparta.kidscafe.domain.cafe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NaverCafeResponseDto {
    private String name;         // 카페 이름
    private String address;      // 카페 주소
    private String region;       // 지역명
    private String contact;      // 연락처
    private String openingHours; // 운영 시간
    private String website;      // 웹사이트 링크
}