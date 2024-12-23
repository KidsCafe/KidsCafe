package com.sparta.kidscafe.domain.user.dto.response;

import com.sparta.kidscafe.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {

  private Long id;           // 회원 ID
  private String email;      // 회원 이메일
  private String name;       // 회원 이름
  private String nickname;   // 회원 닉네임
  private String address;    // 회원 주소
  private String imagePath;  // 프로필 이미지 경로
  private String role;       // 회원 역할 (USER, ADMIN)

  // 기존 생성자에 `public` 추가
  public UserResponseDto(Long id, String email, String name, String nickname, String address, String imagePath, String role) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.nickname = nickname;
    this.address = address;
    this.imagePath = imagePath;
    this.role = role;
  }

  public static UserResponseDto fromEntity(User user) {
    return UserResponseDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .name(user.getName())
        .nickname(user.getNickname())
        .address(user.getAddress())
        .imagePath(user.getImagePath()) // User 엔티티의 imagePath 필드
        .role(user.getRole().name())    // Enum -> String 변환
        .build();
  }
}
