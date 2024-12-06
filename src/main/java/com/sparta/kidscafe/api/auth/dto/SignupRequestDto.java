package com.sparta.kidscafe.api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SignupRequestDto(
	@Email
    @NotNull(message = "이메일은 필수 입력 사항입니다.")
    String email,

    @NotNull(message = "비밀번호를 입력해 주세요")
    String password,

    @NotNull(message = "이름을 입력해주세요.")
    String name,

    @NotNull(message = "주소를 입력해주세요.")
    String address,

    @NotNull(message = "닉네임을 입력해주세요.")
    String nickname,

	@NotNull(message = "")
	String socialLoginType) {

}
