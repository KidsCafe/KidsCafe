package com.sparta.kidscafe.domain.cafe.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;

public class CafeValidationCheck {

  public static void validAdmin(AuthUser authUser) {
    if (authUser.getRoleType() != RoleType.ADMIN) {
      throw new BusinessException(ErrorCode.FORBIDDEN);
    }
  }

  public static void validOwner(AuthUser authUser) {
    if (authUser.getRoleType() != RoleType.OWNER) {
      throw new BusinessException(ErrorCode.FORBIDDEN);
    }
  }

  public static void validUpdateCafe(AuthUser authUser) {
    if (authUser.getRoleType() == RoleType.ADMIN) {
      throw new BusinessException(ErrorCode.FORBIDDEN);
    }
  }

  public static void validMyCafe(AuthUser authUser, Cafe cafe) {
    if (!authUser.getId().equals(cafe.getUser().getId())) {
      throw new BusinessException(ErrorCode.FORBIDDEN);
    }
  }
}
