package com.sparta.kidscafe.api.oauth2.config;

import java.util.Map;

import com.sparta.kidscafe.api.oauth2.provider.OAuth2ProviderProperties;
import com.sparta.kidscafe.api.oauth2.service.OAuth2ProviderType;

public class InMemoryProviderRepository {
	public InMemoryProviderRepository(Map<OAuth2ProviderType, OAuth2ProviderProperties> provider) {
	}
}
