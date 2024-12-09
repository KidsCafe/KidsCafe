package com.sparta.kidscafe.api.auth.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SignupRequestDto(
	@Email(message = "이메일 형식에 맞게 작성해주세요.")
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
	String loginType,

	@NotNull(message = "사장님이실 경우 사장님 체크해주세요")
	String role
) {

}
