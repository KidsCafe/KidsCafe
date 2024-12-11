package com.sparta.kidscafe.domain.cafe.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;

public class CafeValidationCheck {

  public static void validAdmin(AuthUser authUser) {
    if(authUser.getRoleType() != RoleType.ADMIN)
      throw new BusinessException(ErrorCode.FORBIDDEN);
  }

  public static void validCreateCafeByAdmin(AuthUser authUser) {
    validAdmin(authUser);
  }
}
