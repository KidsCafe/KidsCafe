package com.sparta.kidscafe.domain.user.service;

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

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class UserAdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserAdminService userAdminService;

    public UserAdminServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("USER 권한 사용자 목록 조회 성공")
    void getAdminUsers_Success() {
        // Given
        int page = 1;
        int size = 5;

        User user = User.builder()
                .email("user@test.com")
                .name("User")
                .role(RoleType.USER)
                .build();

        List<User> users = Collections.singletonList(user);
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(page - 1, size), users.size());

        when(userRepository.findAllByRole(RoleType.USER, PageRequest.of(page - 1, size))).thenReturn(userPage);

        // When
        ListResponseDto<UserResponseDto> response = userAdminService.getAdminUsers(page, size);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("회원 조회 성공");
        assertThat(response.getData()).isNotEmpty();
        assertThat(response.getData().get(0).getEmail()).isEqualTo("user@test.com");
    }

    @Test
    @DisplayName("페이지 번호가 0이거나 음수일 때 예외 발생")
    void getAdminUsers_InvalidPageNumber() {
        // Given
        int page = 0;
        int size = 5;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userAdminService.getAdminUsers(page, size));

        assertThat(exception.getMessage()).isEqualTo("페이지 번호와 페이지 크기는 1 이상이어야 합니다.");
    }

    @Test
    @DisplayName("페이지 크기가 0이거나 음수일 때 예외 발생")
    void getAdminUsers_InvalidPageSize() {
        // Given
        int page = 1;
        int size = 0;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userAdminService.getAdminUsers(page, size));

        assertThat(exception.getMessage()).isEqualTo("페이지 번호와 페이지 크기는 1 이상이어야 합니다.");
    }

    @Test
    @DisplayName("USER 권한 사용자 목록이 비어있을 때 성공적으로 빈 목록 반환")
    void getAdminUsers_EmptyList() {
        // Given
        int page = 1;
        int size = 5;

        Page<User> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(page - 1, size), 0);

        when(userRepository.findAllByRole(RoleType.USER, PageRequest.of(page - 1, size))).thenReturn(emptyPage);

        // When
        ListResponseDto<UserResponseDto> response = userAdminService.getAdminUsers(page, size);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("회원 조회 성공");
        assertThat(response.getData()).isEmpty();
    }
}
