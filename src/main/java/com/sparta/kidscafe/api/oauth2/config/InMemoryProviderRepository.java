package com.sparta.kidscafe.api.oauth2.config;

import java.util.HashMap;
import java.util.Map;

import com.sparta.kidscafe.api.oauth2.provider.OAuth2Provider;

// OAuth2Provider 를 저장해주는 저장소
public class InMemoryProviderRepository {

	private final Map<String, OAuth2Provider> providers;

	public InMemoryProviderRepository(Map<String, OAuth2Provider> providers) {
		this.providers = new HashMap<>(providers);
	}

	public OAuth2Provider findByProviderName(String name) {
		return providers.get(name);
	}
}
