package com.sparta.kidscafe.api.oauth2.service;

import java.util.Arrays;
import java.util.Map;

import com.sparta.kidscafe.api.oauth2.controller.dto.OAuth2UserProfileDto;

public enum OAuth2Attributes {
	// 일단 깃헙만 추가 -> 테스트 완료되면 다른 소셜도 추가 예정
	GITHUB("github"){
		@Override
		public OAuth2UserProfileDto of(Map<String, Object> attributes){
			return OAuth2UserProfileDto.builder()
				.oauthId(String.valueOf(attributes.get("id")))
				.email((String) attributes.get("email"))
				.name((String) attributes.get("name"))
				.build();
		}
	};

	private final String providerName;

	OAuth2Attributes(String name){
		this.providerName = name;
	}

	public static OAuth2UserProfileDto extract(String providerName, Map<String, Object> attributes){
		return Arrays.stream(values())
			.filter(provider -> providerName.equals(provider.providerName))
			.findFirst()
			.orElseThrow(IllegalArgumentException::new)
			.of(attributes);
	}

	public abstract OAuth2UserProfileDto of(Map<String, Object> attributes);
}
