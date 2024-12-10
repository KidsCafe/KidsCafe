package com.sparta.kidscafe.domain.cafe.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CafeResponseDto {

  private Long id;
  private String name;
  private String address;
  private int size;
  private double star;
  private Long reviewCount;
  private String dayOff;
  private boolean multiFamily;
  private boolean existRoom;
  private boolean parking;
  private boolean existRestaurant;
  private String hyperLink;
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
      String dayOff,
      boolean multiFamily,
      boolean existRoom,
      boolean parking,
      boolean existRestaurant,
      String hyperLink,
      LocalTime openedAt,
      LocalTime closedAt) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.size = size;
    this.star = star;
    this.reviewCount = reviewCount;
    this.dayOff = dayOff;
    this.multiFamily = multiFamily;
    this.existRoom = existRoom;
    this.parking = parking;
    this.existRestaurant = existRestaurant;
    this.hyperLink = hyperLink;
    this.openedAt = openedAt;
    this.closedAt = closedAt;
  }
}
