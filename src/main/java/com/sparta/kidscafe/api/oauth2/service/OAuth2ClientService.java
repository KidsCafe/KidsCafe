package com.sparta.kidscafe.api.oauth2.service;

import com.sparta.kidscafe.api.oauth2.controller.dto.OAuthUserProfile;
import com.sparta.kidscafe.api.oauth2.provider.OAuth2ProviderProperties;

public interface OAuth2ClientService {
	String requestAccessToken(String authorizationCode, OAuth2ProviderProperties provider);

	OAuthUserProfile requestUserProfile(String oAuthAccessToken, String providerName, OAuth2ProviderProperties provider);
}
