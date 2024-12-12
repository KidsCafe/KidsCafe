package com.sparta.kidscafe.api.oauth2.service;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.sparta.kidscafe.api.auth.controller.dto.SigninResponseDto;
import com.sparta.kidscafe.api.oauth2.config.InMemoryProviderRepository;
import com.sparta.kidscafe.api.oauth2.controller.dto.OAuth2TokenResponseDto;
import com.sparta.kidscafe.api.oauth2.controller.dto.OAuth2UserProfile;
import com.sparta.kidscafe.api.oauth2.provider.OAuth2Provider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

	private final InMemoryProviderRepository inMemoryProviderRepository;

	public SigninResponseDto signin(String providerName, String code){
		// 프론트에서 넘어온 provider 이름을 통해 InMemoryProviderRepository 에서 OAuth2Provider 가져온다
		OAuth2Provider provider = inMemoryProviderRepository.findByProviderName(providerName);

		// accessToken
		OAuth2TokenResponseDto responseDto = getToken(code, provider);
		// OAuth2UserProfile -> 유저 정보
		OAuth2UserProfile userProfile = getUserProfile(providerName, responseDto, provider);

		return null;
	}

	private OAuth2TokenResponseDto getToken(String code, OAuth2Provider provider) {
		return WebClient.create()
			.post()
			.uri(provider.getTokenUrl())
			.header(header -> {
				header.setBasicAuth(provider.getClientId(), provider.getClientSecret());
				header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
				header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
				header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
			})
			.bodyValue(tokenRequest(code, provider))
			.retrieve()
			.bodyToMono(OAuth2TokenResponseDto.class)
			.block();
	}

	private MultiValueMap<String, String> tokenRequest(String code, OAuth2Provider provider) {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("code", code);
		formData.add("grant_type", "authorization_code");
		formData.add("redirect_uri", provider.getRedirectUrl());

		return formData;
	}
}
