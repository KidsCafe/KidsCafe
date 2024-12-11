package com.sparta.kidscafe.api.oauth2.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.kidscafe.api.auth.controller.dto.SigninResponseDto;
import com.sparta.kidscafe.api.auth.service.AuthService;
import com.sparta.kidscafe.api.oauth2.config.InMemoryProviderRepository;
import com.sparta.kidscafe.api.oauth2.controller.dto.OAuthUserProfile;
import com.sparta.kidscafe.api.oauth2.provider.OAuth2ProviderProperties;
import com.sparta.kidscafe.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

	private final UserRepository userRepository;
	private final InMemoryProviderRepository inMemoryProviderRepository;
	private final OAuth2ClientService oAuth2ClientService;
	private final AuthService authService;

	public SigninResponseDto signinOrRegister(String providerName, String authorizationCode) throws JsonProcessingException {
		OAuth2ProviderType providerType = OAuth2ProviderType.of(providerName);
		OAuth2ProviderProperties provider = inMemoryProviderRepository.findProvider(providerType);
		String oAuthAccessToken = oAuth2ClientService.requestAccessToken(authorizationCode, provider);
		OAuthUserProfile oAuthUserProfile = oAuth2ClientService.requestUserProfile(oAuthAccessToken, providerName, provider);

		if(!userRepository.existsByOauthId(oAuthUserProfile.getOauthId())){
			String oauthId = oAuthUserProfile.getOauthId();
			String email = oAuthUserProfile.getEmail();
			String name = oAuthUserProfile.getName();

			authService.signupWithOAuth(oauthId, email, name);
		}
		return authService.signinWithOAuth(oAuthUserProfile.getEmail());
	}
}
