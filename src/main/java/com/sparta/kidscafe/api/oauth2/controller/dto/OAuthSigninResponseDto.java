package com.sparta.kidscafe.api.oauth2.controller.dto;

import com.sparta.kidscafe.common.enums.LoginType;
import com.sparta.kidscafe.common.enums.RoleType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuthSigninResponseDto {

	private Long id;
	private String name;
	private String email;
	private String password;
	private String address;
	private String nickname;
	private RoleType roleType;
	private LoginType loginType;
	private String accessToken;

	@Builder
	public OAuthSigninResponseDto(Long id, String name, String email,
		String password, String address, String nickname, RoleType roleType,
		LoginType loginType, String accessToken) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.address = address;
		this.nickname = nickname;
		this.roleType = roleType;
		this.loginType = loginType;
		this.accessToken = accessToken;
	}
}
