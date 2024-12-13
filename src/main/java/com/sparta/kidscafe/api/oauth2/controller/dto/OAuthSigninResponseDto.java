package com.sparta.kidscafe.api.oauth2.controller.dto;

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
	private RoleType roleType;
	private String accessToken;

	@Builder
	public OAuthSigninResponseDto(Long id, String name, String email, RoleType roleType, String accessToken) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.roleType = roleType;
		this.accessToken = accessToken;
	}
}
