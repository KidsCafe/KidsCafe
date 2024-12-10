package com.sparta.kidscafe.api.oauth2.config;

import java.util.HashMap;
import java.util.Map;

import com.sparta.kidscafe.api.oauth2.provider.OAuth2ProviderProperties;
import com.sparta.kidscafe.api.oauth2.service.OAuth2ProviderType;

public class InMemoryProviderRepository {

	private final Map<OAuth2ProviderType, OAuth2ProviderProperties> providers;

	public InMemoryProviderRepository(Map<OAuth2ProviderType, OAuth2ProviderProperties> providers) {
		this.providers = new HashMap<>(providers);
	}

	public OAuth2ProviderProperties findProvider(OAuth2ProviderType name){
		return providers.get(name);
	}
}
