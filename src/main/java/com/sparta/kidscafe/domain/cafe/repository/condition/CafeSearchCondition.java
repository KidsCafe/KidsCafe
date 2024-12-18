package com.sparta.kidscafe.domain.cafe.repository.condition;

import java.time.LocalTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(builderMethodName = "createBuilder")
public class CafeSearchCondition extends CafeSearchPageCondition {

  private String name;
  private String region;
  private Integer size;
  private String ageGroup;
  private Integer minPrice;
  private Integer maxPrice;
  private Double minStar;
  private Double maxStar;
  private Boolean parking;
  private Boolean opening;
  private Boolean existRestaurant;
  private Boolean existRoom;
  private Boolean adultPrice;
  private Boolean multiFamily;
  private LocalTime openedAt;
  private LocalTime closedAt;
  private Long userId;
  private Long lon;
  private Long lat;
  private Double radiusMeter;
}
