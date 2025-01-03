package com.sparta.kidscafe.api.oauth2.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.kidscafe.common.enums.LoginType;
import com.sparta.kidscafe.common.enums.RoleType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OAuthSigninResponseDto {

	private Long id;
	private String name;
	private String email;
	private String nickname;
	private RoleType roleType;
	private LoginType loginType;
	private String accessToken;

	@Builder
	public OAuthSigninResponseDto(Long id, String name, String email, String nickname, RoleType roleType,
		LoginType loginType, String accessToken) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.nickname = nickname;
		this.roleType = roleType;
		this.loginType = loginType;
		this.accessToken = accessToken;
	}
}
