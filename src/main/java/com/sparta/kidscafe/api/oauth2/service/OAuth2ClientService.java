package com.sparta.kidscafe.api.oauth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.kidscafe.api.oauth2.controller.dto.OAuthUserProfile;
import com.sparta.kidscafe.api.oauth2.provider.OAuth2Provider;

public interface OAuth2ClientService {

	String requestAccessToken(String code, OAuth2Provider provider) throws JsonProcessingException;

	OAuthUserProfile requestUserProfile(String accessToken, String providerName,
		OAuth2Provider provider) throws JsonProcessingException;
}
