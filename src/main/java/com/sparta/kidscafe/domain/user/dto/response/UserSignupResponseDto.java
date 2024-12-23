package com.sparta.kidscafe.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignupResponseDto {

  private Long id;
  private String email;
  private String name;
  private String role;
}
