package com.sparta.kidscafe.domain.cafe.dto.request;

import com.sparta.kidscafe.domain.cafe.repository.condition.CafeSearchCondition;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CafeSearchRequestDto extends CafeSearchPageRequestDto {

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
  private Boolean existLesson;
  private Boolean adultPrice;
  private Boolean multiFamily;
  private String openedAt;
  private String closedAt;
  private Long userId;
  private Double lon;
  private Double lat;
  private Double radiusMeter;

  public CafeSearchCondition getSearchCondition() {
    return CafeSearchCondition.createBuilder()
        .name(name)
        .region(region)
        .size(size)
        .ageGroup(ageGroup)
        .minPrice(minPrice)
        .maxPrice(maxPrice)
        .minStar(minStar)
        .maxStar(maxStar)
        .parking(parking)
        .opening(opening)
        .existRestaurant(existRestaurant)
        .existRoom(existRoom)
        .existLesson(existLesson)
        .multiFamily(multiFamily)
        .openedAt(StringUtils.hasText(openedAt) ? LocalTime.parse(openedAt) : null)
        .closedAt(StringUtils.hasText(closedAt) ? LocalTime.parse(closedAt) : null)
        .userId(userId)
        .lon(lon)
        .lat(lat)
        .radiusMeter(radiusMeter)
        .sortBy(getSortBy())
        .asc(isAsc())
        .pageable(getPageable())
        .build();
  }
}
