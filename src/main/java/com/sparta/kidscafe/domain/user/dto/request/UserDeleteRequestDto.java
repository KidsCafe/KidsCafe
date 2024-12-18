package com.sparta.kidscafe.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor  // 기본 생성자 추가
@AllArgsConstructor
public class UserDeleteRequestDto {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
