package com.sparta.kidscafe.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TargetType {
  FEE("입장료"),
  ROOM("방");

  private final String name;

  @JsonCreator
  public static TargetType getAgeGroup(String name) {
    for (TargetType targetType : TargetType.values()) {
      if (targetType.getName().equals(name)) {
        return targetType;
      }
    }
    return FEE;
  }

  @JsonValue
  public String getName() {
    return name;
  }
}
