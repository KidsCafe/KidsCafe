package com.sparta.kidscafe.domain.user.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ResponseDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.common.util.PasswordEncoder;
import com.sparta.kidscafe.domain.user.dto.request.UserProfileUpdateRequestDto;
import com.sparta.kidscafe.domain.user.dto.response.UserProfileResponseDto;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class UserProfileServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserProfileService userProfileService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userProfileService = new UserProfileService(userRepository, passwordEncoder);
    }

    @Test
    @DisplayName("프로필 조회 성공")
    void getUserProfile_Success() {
        // Given
        AuthUser authUser = new AuthUser(1L, "0411khj@naver.com", RoleType.USER);
        User user = User.builder()
                .id(1L)
                .email("0411khj@naver.com")
                .name("김혜진")
                .nickname("호홍")
                .address("부산광역시 남구")
                .password("password123")
                .role(RoleType.USER)
                .build();

        when(userRepository.findById(authUser.getId())).thenReturn(Optional.of(user));

        // When
        ResponseDto<UserProfileResponseDto> response = userProfileService.getUserProfile(authUser);

        // Then
        assertThat(response.getMessage()).isEqualTo("회원 조회 성공");
        assertThat(response.getData().getName()).isEqualTo("김혜진");
        assertThat(response.getData().getNickname()).isEqualTo("호홍");
        assertThat(response.getData().getAddress()).isEqualTo("부산광역시 남구");
    }

    @Test
    @DisplayName("프로필 조회 실패 - 사용자 정보 없음")
    void getUserProfile_Fail_UserNotFound() {
        // Given
        AuthUser authUser = new AuthUser(1L, "0411khj@naver.com", RoleType.USER);

        when(userRepository.findById(authUser.getId())).thenReturn(Optional.empty());

        // When
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userProfileService.getUserProfile(authUser);
        });

        // Then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
        assertThat(exception.getMessage()).isEqualTo("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("프로필 수정 성공")
    void updateUserProfile_Success() {
        // Given
        AuthUser authUser = new AuthUser(1L, "0411khj@naver.com", RoleType.USER);
        UserProfileUpdateRequestDto requestDto = new UserProfileUpdateRequestDto(
                "김혜진", "힝", "부산광역시 해운대구", "newPassword123");

        User user = User.builder()
                .id(1L)
                .email("0411khj@naver.com")
                .name("김혜진")
                .nickname("호홍")
                .address("부산광역시 남구")
                .password("password123")
                .role(RoleType.USER)
                .build();

        when(userRepository.findById(authUser.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword123");

        // When
        ResponseDto<UserProfileResponseDto> response = userProfileService.updateUserProfile(authUser, requestDto);

        // Then
        assertThat(response.getMessage()).isEqualTo("프로필 수정 성공");
        assertThat(response.getData().getNickname()).isEqualTo("힝");
        assertThat(response.getData().getAddress()).isEqualTo("부산광역시 해운대구");
    }

    @Test
    @DisplayName("프로필 수정 실패 - 사용자 정보 없음")
    void updateUserProfile_Fail_UserNotFound() {
        // Given
        AuthUser authUser = new AuthUser(1L, "0411khj@naver.com", RoleType.USER);
        UserProfileUpdateRequestDto requestDto = new UserProfileUpdateRequestDto(
                "김혜진", "힝", "부산광역시 해운대구", "newPassword123");

        when(userRepository.findById(authUser.getId())).thenReturn(Optional.empty());

        // When
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userProfileService.updateUserProfile(authUser, requestDto);
        });

        // Then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
        assertThat(exception.getMessage()).isEqualTo("사용자를 찾을 수 없습니다.");
    }
}