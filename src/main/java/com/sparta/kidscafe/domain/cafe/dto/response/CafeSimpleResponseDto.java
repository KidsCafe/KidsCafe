package com.sparta.kidscafe.domain.cafe.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class CafeSimpleResponseDto {
  private Long id;
  private String name;
  private double star;
  private Long reviewCount;
  private String dayOff;
  private String hyperLink;
  private LocalTime openedAt;
  private LocalTime closedAt;

  @QueryProjection
  public CafeSimpleResponseDto(
      Long id,
      String name,
      double star,
      Long reviewCount,
      String dayOff,
      String hyperLink,
      LocalTime openedAt,
      LocalTime closedAt) {
    this.id = id;
    this.name = name;
    this.star = star;
    this.reviewCount = reviewCount;
    this.dayOff = dayOff;
    this.hyperLink = hyperLink;
    this.openedAt = openedAt;
    this.closedAt = closedAt;
  }
}
