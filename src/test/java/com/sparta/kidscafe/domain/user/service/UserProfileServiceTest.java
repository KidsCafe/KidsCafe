package com.sparta.kidscafe.domain.user.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ResponseDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.common.util.PasswordEncoder;
import com.sparta.kidscafe.domain.user.dto.request.UserDeleteRequestDto;
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
import static org.mockito.Mockito.*;

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

    private User createUser() {
        return User.builder()
                .id(1L)
                .email("0411khj@naver.com")
                .name("김혜진")
                .nickname("호홍")
                .address("부산광역시 남구")
                .password("encodedPassword")
                .role(RoleType.USER)
                .build();
    }

    @Test
    @DisplayName("프로필 조회 성공")
    void getUserProfile_Success() {
        AuthUser authUser = new AuthUser(1L, "0411khj@naver.com", RoleType.USER);
        User user = createUser();

        when(userRepository.findById(authUser.getId())).thenReturn(Optional.of(user));

        ResponseDto<UserProfileResponseDto> response = userProfileService.getUserProfile(authUser);

        assertThat(response.getMessage()).isEqualTo("회원 조회 성공");
        assertThat(response.getData().getName()).isEqualTo("김혜진");
    }

    @Test
    @DisplayName("프로필 조회 실패 - 사용자 정보 없음")
    void getUserProfile_Fail_UserNotFound() {
        AuthUser authUser = new AuthUser(1L, "0411khj@naver.com", RoleType.USER);

        when(userRepository.findById(authUser.getId())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> userProfileService.getUserProfile(authUser));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("프로필 수정 성공")
    void updateUserProfile_Success() {
        AuthUser authUser = new AuthUser(1L, "0411khj@naver.com", RoleType.USER);
        User user = createUser();
        UserProfileUpdateRequestDto requestDto = new UserProfileUpdateRequestDto(
                "김혜진", "힝", "부산광역시 해운대구", "newPassword123");

        when(userRepository.findById(authUser.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword123");

        ResponseDto<UserProfileResponseDto> response = userProfileService.updateUserProfile(authUser, requestDto);

        assertThat(response.getMessage()).isEqualTo("프로필 수정 성공");
        assertThat(response.getData().getNickname()).isEqualTo("힝");
    }

    @Test
    @DisplayName("프로필 수정 실패 - 사용자 정보 없음")
    void updateUserProfile_Fail_UserNotFound() {
        AuthUser authUser = new AuthUser(1L, "0411khj@naver.com", RoleType.USER);
        UserProfileUpdateRequestDto requestDto = new UserProfileUpdateRequestDto(
                "김혜진", "힝", "부산광역시 해운대구", "newPassword123");

        when(userRepository.findById(authUser.getId())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> userProfileService.updateUserProfile(authUser, requestDto));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void deleteUser_Success() {
        AuthUser authUser = new AuthUser(1L, "0411khj@naver.com", RoleType.USER);
        User user = createUser();
        UserDeleteRequestDto requestDto = new UserDeleteRequestDto("password123");

        when(userRepository.findById(authUser.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).thenReturn(true);

        userProfileService.deleteUser(authUser, requestDto);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    @DisplayName("회원 탈퇴 실패 - 사용자 정보 없음")
    void deleteUser_Fail_UserNotFound() {
        AuthUser authUser = new AuthUser(1L, "0411khj@naver.com", RoleType.USER);
        UserDeleteRequestDto requestDto = new UserDeleteRequestDto("password123");

        when(userRepository.findById(authUser.getId())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> userProfileService.deleteUser(authUser, requestDto));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("회원 탈퇴 실패 - 비밀번호 불일치")
    void deleteUser_Fail_InvalidPassword() {
        AuthUser authUser = new AuthUser(1L, "0411khj@naver.com", RoleType.USER);
        User user = createUser();
        UserDeleteRequestDto requestDto = new UserDeleteRequestDto("wrongPassword");

        when(userRepository.findById(authUser.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> userProfileService.deleteUser(authUser, requestDto));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_PASSWORD);
    }
}