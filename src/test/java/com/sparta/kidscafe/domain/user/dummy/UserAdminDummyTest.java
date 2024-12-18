package com.sparta.kidscafe.domain.user.dummy;

import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.user.dto.response.UserResponseDto;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.dummy.DummyUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAdminDummyTest {

    @Test
    @DisplayName("ADMIN 권한을 가진 더미 사용자 목록 생성 테스트")
    void createDummyUserList_Admin() {
        // Given
        int size = 2;
        RoleType roleType = RoleType.ADMIN;

        // When
        List<User> dummyUsers = DummyUser.createDummyUsers(roleType, size);

        // Then
        assertThat(dummyUsers).isNotNull();
        assertThat(dummyUsers.size()).isEqualTo(size);
        assertThat(dummyUsers.get(0).getRole()).isEqualTo(roleType);
    }

    @Test
    @DisplayName("ADMIN 권한을 가진 사용자 DTO 목록 변환 테스트")
    void createDummyUserResponseList() {
        // Given
        int size = 2;
        RoleType roleType = RoleType.ADMIN;
        List<User> dummyUsers = DummyUser.createDummyUsers(roleType, size);

        // When
        List<UserResponseDto> userResponseDtos = dummyUsers.stream()
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());

        ListResponseDto<UserResponseDto> responseDto = ListResponseDto.success(
                userResponseDtos,
                org.springframework.http.HttpStatus.OK,
                "회원 조회 성공"
        );

        // Then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getStatus()).isEqualTo(200);
        assertThat(responseDto.getMessage()).isEqualTo("회원 조회 성공");
        assertThat(responseDto.getData().size()).isEqualTo(size);
    }
}
