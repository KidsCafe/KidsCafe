package com.sparta.kidscafe.common.util.valid;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;

public class AuthValidationCheck {

  public static void validNotUser(AuthUser authUser) {
    if (authUser.getRoleType() == RoleType.USER) {
      throw new BusinessException(ErrorCode.FORBIDDEN);
    }
  }

  public static void validUser(AuthUser authUser) {
    if (authUser.getRoleType() != RoleType.USER) {
      throw new BusinessException(ErrorCode.FORBIDDEN);
    }
  }

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
}
