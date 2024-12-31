package com.sparta.kidscafe.domain.cafe.dto.response;

import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CafeViewCountResponseDto {

  Long cafeId;
  String cafeName;
  Integer viewCount;

  public static CafeViewCountResponseDto from(Cafe cafe, Integer viewCount) {
    return CafeViewCountResponseDto.builder()
        .cafeId(cafe.getId())
        .cafeName(cafe.getName())
        .viewCount(viewCount)
        .build();
  }
}
