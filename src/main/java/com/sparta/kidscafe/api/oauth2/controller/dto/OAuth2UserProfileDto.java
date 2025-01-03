package com.sparta.kidscafe.api.oauth2.controller.dto;

import com.sparta.kidscafe.common.enums.LoginType;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.user.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuth2UserProfileDto {

	private final String oauthId;
	private final String name;
	private final String email;
	private final String address;
	private final String nickname;
	private final LoginType loginType;
	private final String provider;

	@Builder
	public OAuth2UserProfileDto(String oauthId, String name, String email, String address, String nickname, LoginType loginType, String provider) {
		this.oauthId = oauthId;
		this.name = name;
		this.email = email;
		this.address = address;
		this.nickname = nickname;
		this.loginType = loginType;
		this.provider = provider;
	}

	public User toUser(){
		return User.builder()
			.oauthId(oauthId)
			.email(email != null ? email : "")
			.password("UNKNOWN")
			.nickname(nickname != null ? nickname : "")
			.name(name != null ? name : "")
			.address("UNKNOWN")
			.role(RoleType.USER)
			.loginType(loginType != null ? loginType :LoginType.BASIC)
			.provider(provider)
			.build();
	}
}
