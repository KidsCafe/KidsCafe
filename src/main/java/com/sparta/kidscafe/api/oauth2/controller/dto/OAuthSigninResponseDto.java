package com.sparta.kidscafe.api.oauth2.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthSigninResponseDto {

	private String oAuthId;
	private String email;
	private String name;
	private String accessToken;
}
