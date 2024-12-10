package com.sparta.kidscafe.domain.cafe.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchSortBy {
  CAFE_NAME,
  REVIEW_COUNT,
  REVIEW_AVG,
  ROOM_EXIST,
  OPENED_AT,
  CLOSED_AT;

  @JsonCreator
  public static SearchSortBy getAgeGroup(String name) {
    for (SearchSortBy sortBy : SearchSortBy.values()) {
      if (sortBy.toString().equals(name)) {
        return sortBy;
      }
    }
    return CAFE_NAME;
  }

  @JsonValue
  public String getName() {
    return toString();
  }
}
