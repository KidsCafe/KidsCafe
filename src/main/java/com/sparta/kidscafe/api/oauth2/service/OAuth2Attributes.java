package com.sparta.kidscafe.api.oauth2.service;

import java.util.Arrays;
import java.util.Map;

import com.sparta.kidscafe.api.oauth2.controller.dto.OAuthUserProfile;

public enum OAuth2Attributes {
	KAKAO("kakao"){
		@Override
		public OAuthUserProfile of(Map<String, Object> attributes){
			Map<String, Object> containEmailResponse = ((Map<String, Object>)attributes.get("kakao_account"));
			Map<String, Object> containNameResponse = ((Map<String, Object>)attributes.get("properties"));
			return OAuthUserProfile.builder()
				.oauthId(attributes.get("id").toString())
				.email((String)containEmailResponse.get("email"))
				.name((String)containNameResponse.get("nickname"))
				.build();
		}
	},
	NAVER("naver") {
		@Override
		public OAuthUserProfile of(Map<String, Object> attributes) {
			Map<String, Object> response = (Map<String, Object>)attributes.get("response");
			return OAuthUserProfile.builder()
				.oauthId((String)response.get("id"))
				.email((String)response.get("email"))
				.name((String)response.get("name"))
				.build();
		}
	},
	GOOGLE("google") {
		@Override
		public OAuthUserProfile of(Map<String, Object> attributes) {
			return OAuthUserProfile.builder()
				.oauthId((String)attributes.get("id"))
				.email((String)attributes.get("email"))
				.name((String)attributes.get("name"))
				.build();
		}
	};

	private final String providerName;

	public abstract OAuthUserProfile of(Map<String, Object> attributes);

	OAuth2Attributes(String name){
		this.providerName = name;
	}

	public static OAuthUserProfile extract(String providerName, Map<String, Object> attributes) {
		return Arrays.stream(values())
			.filter(provider -> providerName.equals(provider.providerName))
			.findFirst()
			.orElseThrow(IllegalAccessError::new)
			.of(attributes);
	}
}
