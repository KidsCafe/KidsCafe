package com.sparta.kidscafe.api.oauth2.controller.dto;

import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.user.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuth2UserProfile {

	private final String oauthId;
	private final String email;
	private final String name;

	@Builder
	public OAuth2UserProfile(String oauthId, String email, String name) {
		this.oauthId = oauthId;
		this.email = email;
		this.name = name;
	}

	public User toUser(){
		return User.builder()
			.oauthId(oauthId)
			.email(email)
			.name(name)
			.role(RoleType.USER)
			.build();
	}
}
