package com.sparta.kidscafe.domain.user.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.user.dto.response.UserResponseDto;
import com.sparta.kidscafe.domain.user.service.UserAdminService;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserAdminController {

  private final UserAdminService userAdminService;

  @GetMapping("/api/admin/users")
  public ResponseEntity<ListResponseDto<UserResponseDto>> getAdminUsers(
      @Auth AuthUser authUser,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    // 인증된 사용자 정보 활용 (예: 관리자 권한 확인)
    if (authUser.getRoleType() != RoleType.ADMIN) {
      throw new BusinessException(ErrorCode.FORBIDDEN);
    }

    // UserAdminService에서 ListResponseDto 반환
    ListResponseDto<UserResponseDto> responseDto = userAdminService.getAdminUsers(page, size);

    return ResponseEntity.ok(responseDto);
  }
}