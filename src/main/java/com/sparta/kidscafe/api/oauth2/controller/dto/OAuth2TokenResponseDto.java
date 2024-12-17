package com.sparta.kidscafe.api.oauth2.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// OAuth 서버와 통신을 통해 accessToken 을 하는 responseDto
@Getter
@NoArgsConstructor
public class OAuth2TokenResponseDto {

	@JsonProperty("access_token")
	private String accessToken;

	private String scope;

	@JsonProperty("token_type")
	private String tokenType;

	@Builder
	public OAuth2TokenResponseDto(String accessToken, String scope, String tokenType) {
		this.accessToken = accessToken;
		this.scope = scope;
		this.tokenType = tokenType;
	}
}
