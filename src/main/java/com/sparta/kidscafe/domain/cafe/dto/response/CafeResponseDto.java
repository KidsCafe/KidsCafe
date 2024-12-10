package com.sparta.kidscafe.domain.cafe.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
public class CafeResponseDto {

  private Long id;
  private String name;
  private String address;
  private int size;
  private double star;
  private Long reviewCount;
  private boolean existRoom;
  private boolean parking;
  private boolean restaurantExists;
  private LocalTime openedAt;
  private LocalTime closedAt;

  @QueryProjection
  public CafeResponseDto(
      Long id,
      String name,
      String address,
      int size,
      double star,
      Long reviewCount,
      boolean existRoom, // boolean
      boolean parking,
      boolean restaurantExists,
      LocalTime openedAt,
      LocalTime closedAt) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.size = size;
    this.star = star;
    this.reviewCount = reviewCount;
    this.existRoom = existRoom;
    this.parking = parking;
    this.restaurantExists = restaurantExists;
    this.openedAt = openedAt;
    this.closedAt = closedAt;
  }
}
