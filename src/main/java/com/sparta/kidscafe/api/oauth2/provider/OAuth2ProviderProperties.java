package com.sparta.kidscafe.api.oauth2.provider;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuth2ProviderProperties {

	private final String clientId;
	private final String clientSecret;
	private final String redirectUrl;
	private final String tokenUrl;
	private final String userInfoUrl;

	@Builder
	public OAuth2ProviderProperties(String clientId, String clientSecret, String redirectUrl, String tokenUrl,
		String userInfoUrl) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.redirectUrl = redirectUrl;
		this.tokenUrl = tokenUrl;
		this.userInfoUrl = userInfoUrl;
	}

	// public OAuth2ProviderProperties(OAuth2Propertiest.User user, )

}
