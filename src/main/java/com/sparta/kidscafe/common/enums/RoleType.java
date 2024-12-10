package com.sparta.kidscafe.common.enums;

import java.util.Arrays;

import com.sun.jdi.request.InvalidRequestStateException;

public enum RoleType {

  USER,
  ADMIN,
  OWNER;

  public static RoleType of(String roleType) {
    return Arrays.stream(RoleType.values())
        .filter(r -> r.name().equalsIgnoreCase(roleType))
        .findFirst()
        .orElseThrow(() -> new InvalidRequestStateException("유효하지 않은 roleType: " + roleType));
  }
}
