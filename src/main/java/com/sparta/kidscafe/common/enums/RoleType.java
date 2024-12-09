package com.sparta.kidscafe.common.enums;

import java.util.Arrays;

import com.sun.jdi.request.InvalidRequestStateException;

public enum RoleType {
  /**
   * 일반 사용자 권한
   */
  USER,
  /**
   * 관리자 권한
   */
  ADMIN,
  /**
   * 관리자 권한
   */
  OWNER;

  public static RoleType of(String roleType) {
    return Arrays.stream(RoleType.values())
        .filter(r -> r.name().equalsIgnoreCase(roleType))
        .findFirst()
        .orElseThrow(() -> new InvalidRequestStateException("유효하지 않은 roleType: " + roleType));
  }
}
