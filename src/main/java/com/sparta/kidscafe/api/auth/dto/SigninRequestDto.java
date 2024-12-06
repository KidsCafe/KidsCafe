package com.sparta.kidscafe.api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SigninRequestDto(
	@Email
	@NotNull(message = "이메일을 입력해주세요.")
	String email,

	@NotNull(message = "비밀번호를 입력하세요")
    String password) {

}
