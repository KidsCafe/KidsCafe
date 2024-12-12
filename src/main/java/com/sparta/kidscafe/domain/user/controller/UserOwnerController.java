package com.sparta.kidscafe.domain.user.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.user.dto.response.UserResponseDto;
import com.sparta.kidscafe.domain.user.service.UserOwnerService;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserOwnerController {

    private final UserOwnerService userOwnerService;

    @GetMapping("/api/owners/users")
    public ResponseEntity<ListResponseDto<UserResponseDto>> getUsersWhoFavoritedOwner(
            @Auth AuthUser authUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // 인증된 사용자 정보 활용 (OWNER 권한 확인)
        if (authUser.getRoleType() != RoleType.OWNER) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        // UserOwnerService에서 ListResponseDto 반환
        ListResponseDto<UserResponseDto> responseDto = userOwnerService.getUsersWhoFavoritedOwner(authUser, page, size);

        return ResponseEntity.ok(responseDto);
    }
}