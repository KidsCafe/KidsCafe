package com.sparta.kidscafe.api.oauth2.adapter;

import java.util.HashMap;
import java.util.Map;

import com.sparta.kidscafe.api.oauth2.config.OAuth2Properties;
import com.sparta.kidscafe.api.oauth2.provider.OAuth2ProviderProperties;
import com.sparta.kidscafe.api.oauth2.service.OAuth2ProviderType;

public class OAuth2Adapter {

	private OAuth2Adapter(){

	}

	public static Map<OAuth2ProviderType, OAuth2ProviderProperties> getOAuth2Provider(OAuth2Properties properties) {
		Map<OAuth2ProviderType, OAuth2ProviderProperties> providers = new HashMap<>();
		properties.getUser().forEach((key, value) -> {
			OAuth2ProviderType providerType = OAuth2ProviderType.of(key);
			providers.put(providerType, new OAuth2ProviderProperties(value, properties.getProvider().get(key)));
		});
		return providers;
	}
}
