package com.sparta.kidscafe.domain.user.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateRequestDto {

    @Size(max = 30, message = "이름은 최대 30자까지 가능합니다.")
    private String name;

    @Size(max = 30, message = "닉네임은 최대 30자까지 가능합니다.")
    private String nickname;

    @Size(max = 50, message = "주소는 최대 50자까지 가능합니다.")
    private String address;

    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상, 20자 이하로 입력해야 합니다.")
    private String password;
}
