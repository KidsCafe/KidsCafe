package com.sparta.kidscafe.api.oauth2.service;

public enum OAuth2ProviderType {
	GOOGLE("google"),
	KAKAO("kakao"),
	NAVER("naver");

	private final String providerType;

	OAuth2ProviderType(String providerType) {
		this.providerType = providerType;
	}

	public static OAuth2ProviderType of(String provideName){
		for(OAuth2ProviderType providerType : OAuth2ProviderType.values()){
			if(providerType.providerType.equals(provideName)){
				return providerType;
			}
		}
		throw new IllegalArgumentException("제공하지 않는 Provider 입니다.");
	}
}
