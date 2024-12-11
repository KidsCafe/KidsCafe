package com.sparta.kidscafe.api.oauth2.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthUserProfile {

	private String oauthId;
	private String email;
	private String name;
}
