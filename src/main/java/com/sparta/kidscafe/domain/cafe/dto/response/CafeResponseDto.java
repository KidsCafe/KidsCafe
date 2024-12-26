package com.sparta.kidscafe.domain.cafe.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(builderMethodName = "createBuilder")
public class CafeResponseDto extends CafeSimpleResponseDto {

  private String address;
  private Double longitude;
  private Double latitude;
  private int size;
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
    setId(id);
    setName(name);
    this.address = address;
    this.longitude = longitude;
    this.latitude = latitude;
    this.size = size;
    setStar(star);
    setReviewCount(reviewCount);
    setDayOff(dayOff);
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
    setHyperLink(hyperLink);
    setOpenedAt(openedAt);
    setClosedAt(closedAt);
  }
}
