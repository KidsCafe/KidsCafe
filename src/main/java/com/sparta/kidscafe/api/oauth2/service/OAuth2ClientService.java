package com.sparta.kidscafe.api.oauth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.kidscafe.api.oauth2.controller.dto.OAuthUserProfile;
import com.sparta.kidscafe.api.oauth2.provider.OAuth2ProviderProperties;

public interface OAuth2ClientService {

	String requestAccessToken(String code, OAuth2ProviderProperties provider) throws JsonProcessingException;

	OAuthUserProfile requestUserProfile(String accessToken, String providerName,
		OAuth2ProviderProperties provider) throws JsonProcessingException;
}
