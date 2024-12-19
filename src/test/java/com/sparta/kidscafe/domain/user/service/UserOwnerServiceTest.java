package com.sparta.kidscafe.domain.user.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.user.dto.response.UserResponseDto;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class UserOwnerServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserOwnerService userOwnerService;

    public UserOwnerServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("회원 조회 성공")
    void getUsersWhoFavoritedOwner_Success() {
        // GIVEN
        AuthUser authUser = new AuthUser(1L, "owner@test.com", RoleType.OWNER);
        PageRequest pageRequest = PageRequest.of(0, 2);

        List<User> users = List.of(
                new User(1L, "0411khj@naver.com", RoleType.USER),
                new User(2L, "0411khj@naver.com", RoleType.USER)
        );

        Page<User> userPage = new PageImpl<>(users);

        when(userRepository.findUsersByBookmarks_UserId(1L, pageRequest)).thenReturn(userPage);

        // WHEN
        ListResponseDto<UserResponseDto> response = userOwnerService.getUsersWhoFavoritedOwner(authUser, 1, 2);

        // THEN
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("회원 조회 성공");
        assertThat(response.getData().size()).isEqualTo(2);
        assertThat(response.getData().get(0).getEmail()).isEqualTo("0411khj@naver.com");
    }

    @Test
    @DisplayName("회원 조회 결과 없음")
    void getUsersWhoFavoritedOwner_NoResults() {
        // GIVEN
        AuthUser authUser = new AuthUser(1L, "owner@test.com", RoleType.OWNER);
        PageRequest pageRequest = PageRequest.of(0, 2);

        Page<User> emptyPage = Page.empty();

        when(userRepository.findUsersByBookmarks_UserId(1L, pageRequest)).thenReturn(emptyPage);

        // WHEN
        ListResponseDto<UserResponseDto> response = userOwnerService.getUsersWhoFavoritedOwner(authUser, 1, 2);

        // THEN
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("회원 조회 성공");
        assertThat(response.getData().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("권한 없음 - OWNER만 접근 가능")
    void getUsersWhoFavoritedOwner_NoPermission() {
        // GIVEN
        AuthUser authUser = new AuthUser(1L, "user@test.com", RoleType.USER); // OWNER 권한 아님
        PageRequest pageRequest = PageRequest.of(0, 2);

        // userRepository는 호출되면 비어있는 페이지 반환
        when(userRepository.findUsersByBookmarks_UserId(1L, pageRequest))
                .thenReturn(Page.empty());

        // WHEN & THEN
        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> userOwnerService.getUsersWhoFavoritedOwner(authUser, 1, 2));

        assertThat(exception.getMessage()).isEqualTo("접근 권한이 없습니다.");
    }
}