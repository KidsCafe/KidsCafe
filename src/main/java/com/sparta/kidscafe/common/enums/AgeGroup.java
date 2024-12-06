package com.sparta.kidscafe.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AgeGroup {
  BABY("영유아"),
  CHILDREN("어린이"),
  TEENAGER("청소년"),
  ADULT("성인");

  private final String name;

  public static AgeGroup getAgeGroup(String name) {
    for (AgeGroup ageGroup : AgeGroup.values()) {
      if (ageGroup.getName().equals(name)) {
        return ageGroup;
      }
    }
    return ADULT;
  }
}
