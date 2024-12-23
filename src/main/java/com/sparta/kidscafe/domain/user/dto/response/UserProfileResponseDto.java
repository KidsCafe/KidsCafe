package com.sparta.kidscafe.domain.user.dto.response;

import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserProfileResponseDto {
  private final Long id;
  private final String email;
  private final String name;
  private final String nickname;
  private final String address;
  private final RoleType role;

  @Builder
  private UserProfileResponseDto(Long id, String email, String name, String nickname, String address, RoleType role) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.nickname = nickname;
    this.address = address;
    this.role = role;
  }

  // User 엔티티를 DTO로 변환하는 정적 메서드
  public static UserProfileResponseDto fromEntity(User user) {
    return new UserProfileResponseDto(
        user.getId(),
        user.getEmail(),
        user.getName(),
        user.getNickname(),
        user.getAddress(),
        user.getRole()
    );
  }
}
