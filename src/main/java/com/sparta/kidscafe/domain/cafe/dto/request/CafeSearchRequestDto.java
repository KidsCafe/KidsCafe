package com.sparta.kidscafe.domain.cafe.dto.request;

import com.sparta.kidscafe.domain.cafe.dto.SearchCondition;
import com.sparta.kidscafe.domain.cafe.enums.SearchSortBy;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CafeSearchRequestDto {

  private String name;
  private String region;

  @Positive(message = "카페 크기는 0이상입니다.")
  private int size;

  @NotNull(message = "연령대가 비어있습니다.")
  private String ageGroup;

  @Positive(message = "최소 금액은 0원 이상입니다.")
  private int minPrice;

  @Positive(message = "최대 금액은 0원 이상입니다.")
  private int maxPrice;

  @DecimalMin(value = "0.0", message = "최소 별점은 0.0 이상이어야 합니다.")
  private double minStar;

  @DecimalMax(value = "5.0", message = "최대 별점은 5.0 이하여야 합니다.")
  private double maxStar;
  private boolean parking;
  private boolean opening;
  private boolean existRestaurant;
  private boolean existRoom;
  private boolean adultPrice;
  private boolean multiFamily;

  @Pattern(
      regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$",
      message = "오픈 시간은 'hh:mm' 형식이어야 하며, 00:00부터 23:59까지만 가능합니다."
  )
  private String openedAt;

  @Pattern(
      regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$",
      message = "마감 시간은 'hh:mm' 형식이어야 하며, 00:00부터 23:59까지만 가능합니다."
  )
  private String closedAt;

  private int page;
  private int pageSize;
  private SearchSortBy sortBy;
  private boolean asc;

  public SearchCondition getSearchCondition() {
    return SearchCondition.builder()
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
        .multiFamily(multiFamily)
        .openedAt(LocalTime.parse(openedAt))
        .closedAt(LocalTime.parse(closedAt))
        .sortBy(sortBy)
        .asc(asc)
        .pageable(PageRequest.of(page - 1, pageSize))
        .build();
  }
}
