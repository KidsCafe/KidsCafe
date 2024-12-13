package com.sparta.kidscafe.domain.user.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ResponseDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.user.dto.request.UserProfileUpdateRequestDto;
import com.sparta.kidscafe.domain.user.dto.response.UserProfileResponseDto;
import com.sparta.kidscafe.domain.user.service.UserProfileService;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/api/users/profile")
    public ResponseEntity<ResponseDto<UserProfileResponseDto>> getUserProfile(@Auth AuthUser authUser) {
        // 인증된 사용자 권한 확인
        if (authUser.getRoleType() != RoleType.USER) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        // 서비스 계층에서 프로필 조회
        ResponseDto<UserProfileResponseDto> responseDto = userProfileService.getUserProfile(authUser);

        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/api/users/profile")
    public ResponseEntity<ResponseDto<UserProfileResponseDto>> updateUserProfile(
            @Auth AuthUser authUser,
            @RequestBody @Valid UserProfileUpdateRequestDto requestDto) {
        // 인증된 사용자 권한 확인
        if (authUser.getRoleType() != RoleType.USER) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        // 서비스 계층에서 프로필 수정 처리
        ResponseDto<UserProfileResponseDto> responseDto = userProfileService.updateUserProfile(authUser, requestDto);

        return ResponseEntity.ok(responseDto);
    }

}