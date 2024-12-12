package com.sparta.kidscafe.domain.cafe.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchSortBy {
  NONE,
  CAFE_NAME,
  REVIEW_COUNT,
  REVIEW_AVG,
  ROOM_EXIST,
  OPENED_AT,
  CLOSED_AT;

  @JsonCreator
  public static SearchSortBy getSortBy(String name) {
    for (SearchSortBy sortBy : SearchSortBy.values()) {
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
