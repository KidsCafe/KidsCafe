package com.sparta.kidscafe.domain.cafe.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Builder
@Getter
@NoArgsConstructor
public class CafeResponseDto {

  private Long id;
  private String name;
  private String address;
  private Double longitude;
  private Double latitude;
  private int size;
  private double star;
  private Long reviewCount;
  private String dayOff;
  private boolean multiFamily;
  private boolean parking;
  private boolean existRestaurant;
  private boolean existCareService;
  private boolean existSwimmingPool;
  private boolean existClothesRental;
  private boolean existMonitoring;
  private boolean existFeedingRoom;
  private boolean existOutdoorPlayground;
  private boolean existSafetyGuard;
  private boolean existRoom;
  private boolean existLesson;
  private String hyperLink;
  private LocalTime openedAt;
  private LocalTime closedAt;

  @QueryProjection
  public CafeResponseDto(
      Long id,
      String name,
      String address,
      Double longitude,
      Double latitude,
      int size,
      double star,
      Long reviewCount,
      String dayOff,
      boolean multiFamily,
      boolean parking,
      boolean existRestaurant,
      boolean existCareService,
      boolean existSwimmingPool,
      boolean existClothesRental,
      boolean existMonitoring,
      boolean existFeedingRoom,
      boolean existOutdoorPlayground,
      boolean existSafetyGuard,
      boolean existRoom,
      boolean existLesson,
      String hyperLink,
      LocalTime openedAt,
      LocalTime closedAt) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.longitude = longitude;
    this.latitude = latitude;
    this.size = size;
    this.star = star;
    this.reviewCount = reviewCount;
    this.dayOff = dayOff;
    this.multiFamily = multiFamily;
    this.parking = parking;
    this.existRestaurant = existRestaurant;
    this.existCareService = existCareService;
    this.existSwimmingPool = existSwimmingPool;
    this.existClothesRental = existClothesRental;
    this.existMonitoring = existMonitoring;
    this.existFeedingRoom = existFeedingRoom;
    this.existOutdoorPlayground = existOutdoorPlayground;
    this.existSafetyGuard = existSafetyGuard;
    this.existRoom = existRoom;
    this.existLesson = existLesson;
    this.hyperLink = hyperLink;
    this.openedAt = openedAt;
    this.closedAt = closedAt;
  }
}
