package com.sparta.kidscafe.api.oauth2.provider;

import com.sparta.kidscafe.api.oauth2.config.OAuth2Properties;

import lombok.Builder;
import lombok.Getter;

//
@Getter
public class OAuth2Provider {

	private final String clientId;
	private final String clientSecret;
	private final String redirectUrl;
	private final String tokenUrl;
	private final String userInfoUrl;

	public OAuth2Provider(OAuth2Properties.User user, OAuth2Properties.Provider provider){
		this(user.getClientId(), user.getClientSecret(), user.getRedirectUri(), provider.getTokenUri(), provider.getUserInfoUri());
	}

	@Builder
	public OAuth2Provider(String clientId, String clientSecret, String redirectUrl, String tokenUrl,
		String userInfoUrl) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.redirectUrl = redirectUrl;
		this.tokenUrl = tokenUrl;
		this.userInfoUrl = userInfoUrl;
	}

}
