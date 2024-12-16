package com.sparta.kidscafe.api.oauth2.service;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.sparta.kidscafe.api.oauth2.config.InMemoryProviderRepository;
import com.sparta.kidscafe.api.oauth2.controller.dto.OAuth2TokenResponseDto;
import com.sparta.kidscafe.api.oauth2.controller.dto.OAuth2UserProfileDto;
import com.sparta.kidscafe.api.oauth2.controller.dto.OAuthSigninResponseDto;
import com.sparta.kidscafe.api.oauth2.provider.OAuth2Provider;
import com.sparta.kidscafe.common.util.JwtUtil;
import com.sparta.kidscafe.domain.user.entity.OAuthMember;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

	private final InMemoryProviderRepository inMemoryProviderRepository;
	// private final OAuth2TokenProvider tokenProvider;
	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;

	public OAuthSigninResponseDto signin(HttpServletResponse res, String providerName, String code){
		// 프론트에서 넘어온 provider 이름을 통해 InMemoryProviderRepository 에서 OAuth2Provider 가져온다
		OAuth2Provider provider = inMemoryProviderRepository.findByProviderName(providerName);

		// accessToken
		OAuth2TokenResponseDto tokenResponseDto = getToken(code, provider);
		// OAuth2UserProfile -> 유저 정보
		OAuth2UserProfileDto userProfileDto = getUserProfile(providerName, tokenResponseDto, provider);
		// db 에 저장
		OAuthMember oAuthMember = saveOrUpdate(userProfileDto);

		// jwt 토큰 생성
		String accessToken = jwtUtil.generateAccessTokenForOauth(oAuthMember);
		res.addHeader("Authorization", accessToken);

		return OAuthSigninResponseDto.builder()
			.id(oAuthMember.getId())
			.name(oAuthMember.getName())
			.email(oAuthMember.getEmail())
			.roleType(oAuthMember.getRole())
			.loginType(oAuthMember.getLoginType())
			.accessToken(accessToken)
			.build();
	}

	private User saveOrUpdate(OAuth2UserProfileDto userProfileDto) {
		User user = userRepository.findByOauthId(userProfileDto.getOauthId())
			.map(entity -> entity.update( userProfileDto.getEmail(), userProfileDto.getName(), userProfileDto.getNickname()))
			.orElseGet(userProfileDto::toUser);
		return userRepository.save(user);
	}

	private OAuth2TokenResponseDto getToken(String code, OAuth2Provider provider) {
		return WebClient.create()
			.post()
			.uri(provider.getTokenUrl())
			.headers(header -> {
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

	private OAuth2UserProfileDto getUserProfile(String providerName, OAuth2TokenResponseDto tokenResponseDto, OAuth2Provider provider){
		Map<String, Object> userAttributes = getUserAttributes(provider, tokenResponseDto);

		return OAuth2Attributes.extract(providerName, userAttributes);
	}

	// OAuth2 서버에서 유저 정보 Map 으로 받기
	private Map<String, Object> getUserAttributes(OAuth2Provider provider, OAuth2TokenResponseDto tokenResponseDto){
		return WebClient.create()
			.get()
			.uri(provider.getUserInfoUrl())
			.headers(header -> header.setBearerAuth(tokenResponseDto.getAccessToken()))
			.retrieve()
			.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>(){

			})
			.block();
	}

}
