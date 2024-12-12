package com.sparta.kidscafe.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginResponseDto {

    private String token;
    private String email;
    private String name;
    private String role;
}
