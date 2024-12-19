package com.sparta.kidscafe.domain.user.dummy;

import com.sparta.kidscafe.domain.user.dto.response.UserResponseDto;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.common.enums.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class UserOwnerDummyTest {

    @Test
    @DisplayName("더미 사용자 생성 테스트")
    void createDummyUsers() {
        List<User> users = List.of(
                new User(1L, "0411khj@naver.com", RoleType.USER),
                new User(2L, "0411khj@naver.com", RoleType.USER)
        );

        assertThat(users.size()).isEqualTo(2);
        assertThat(users.get(0).getRole()).isEqualTo(RoleType.USER);
    }

    @Test
    @DisplayName("더미 UserResponseDto 변환 테스트")
    void convertToUserResponseDto() {
        List<User> users = List.of(
                new User(1L, "0411khj@naver.com", RoleType.USER),
                new User(2L, "0411khj@naver.com", RoleType.USER)
        );

        List<UserResponseDto> dtos = users.stream()
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());

        assertThat(dtos.size()).isEqualTo(2);
        assertThat(dtos.get(0).getEmail()).isEqualTo("0411khj@naver.com");
    }
}
