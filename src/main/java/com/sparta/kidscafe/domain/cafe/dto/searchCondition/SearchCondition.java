package com.sparta.kidscafe.domain.cafe.dto.searchCondition;

import java.time.LocalTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(builderMethodName = "createBuilder")
public class SearchCondition extends SearchPageCondition {

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
}
