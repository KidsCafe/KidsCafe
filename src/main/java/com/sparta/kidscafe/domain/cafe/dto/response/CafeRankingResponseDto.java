package com.sparta.kidscafe.domain.cafe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CafeRankingResponseDto {

  private Long cafeId;
  private String cafeName;
  private int viewCount;

}
