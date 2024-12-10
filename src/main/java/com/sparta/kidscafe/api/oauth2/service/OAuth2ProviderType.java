package com.sparta.kidscafe.api.oauth2.service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OAuth2ProviderType {
	GOOGLE("google"),
	KAKAO("kakao"),
	NAVER("naver");

	private final String providerType;

	public static OAuth2ProviderType of(String providerName){
		for(OAuth2ProviderType providerType : OAuth2ProviderType.values()){
			if(providerType.providerType.equals(providerName)){
				return providerType;
			}
		}
		throw new IllegalArgumentException("제공하지 않는 Provider 입니다.");
	}
}
