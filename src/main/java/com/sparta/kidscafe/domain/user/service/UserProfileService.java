package com.sparta.kidscafe.domain.user.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ResponseDto;
import com.sparta.kidscafe.common.util.PasswordEncoder;
import com.sparta.kidscafe.domain.user.dto.request.UserProfileUpdateRequestDto;
import com.sparta.kidscafe.domain.user.dto.response.UserProfileResponseDto;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseDto<UserProfileResponseDto> getUserProfile(AuthUser authUser) {
        // 사용자 정보 조회
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // UserProfileResponseDto 생성 및 반환
        UserProfileResponseDto profileResponse = UserProfileResponseDto.fromEntity(user);

        return ResponseDto.success(
                profileResponse,
                HttpStatus.OK,
                "회원 조회 성공"
        );
    }

    @Transactional
    public ResponseDto<UserProfileResponseDto> updateUserProfile(AuthUser authUser, UserProfileUpdateRequestDto requestDto) {
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        user.updateProfile(requestDto.getName(), requestDto.getNickname(), requestDto.getAddress());

        if (requestDto.getPassword() != null) {
            user.changePassword(passwordEncoder.encode(requestDto.getPassword()));
        }

        UserProfileResponseDto updatedProfile = UserProfileResponseDto.fromEntity(user);

        return ResponseDto.success(
                updatedProfile,
                HttpStatus.OK,
                "프로필 수정 성공"
        );
    }
}