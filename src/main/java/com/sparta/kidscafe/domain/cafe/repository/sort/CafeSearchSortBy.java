package com.sparta.kidscafe.domain.cafe.repository.sort;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CafeSearchSortBy {
  NONE,
  CAFE_NAME,
  REVIEW_COUNT,
  REVIEW_AVG,
  ROOM_EXIST,
  OPENED_AT,
  CLOSED_AT;

  @JsonCreator
  public static CafeSearchSortBy getSortBy(String name) {
    for (CafeSearchSortBy sortBy : CafeSearchSortBy.values()) {
      if (sortBy.toString().equals(name)) {
        return sortBy;
      }
    }
    return NONE;
  }

  @JsonValue
  public String getSortBy() {
    return toString();
  }
}
