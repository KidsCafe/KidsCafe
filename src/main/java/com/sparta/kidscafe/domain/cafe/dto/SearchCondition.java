package com.sparta.kidscafe.domain.cafe.dto;

import com.sparta.kidscafe.domain.cafe.enums.SearchSortBy;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Builder
@Getter
public class SearchCondition {

  private String name;
  private String region;
  private int size;
  private String ageGroup;
  private int minPrice;
  private int maxPrice;
  private double minStar;
  private double maxStar;
  private boolean parking;
  private boolean opening;
  private boolean existRestaurant;
  private boolean existRoom;
  private boolean adultPrice;
  private boolean multiFamily;
  private LocalTime openedAt;
  private LocalTime closedAt;
  private Pageable pageable;
  private SearchSortBy sortBy;
  private boolean asc;
}
