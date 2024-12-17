package com.sparta.kidscafe.api.oauth2.adapter;

import java.util.HashMap;
import java.util.Map;

import com.sparta.kidscafe.api.oauth2.config.OAuth2Properties;
import com.sparta.kidscafe.api.oauth2.provider.OAuth2Provider;

public class OAuth2Adapter {

	private OAuth2Adapter(){}

	// OAuth2Properties 를 OAuth2Provider 로 변환해준다
	public static Map<String, OAuth2Provider> getOAuth2Providers(OAuth2Properties properties) {
		Map<String, OAuth2Provider> oauth2Provider = new HashMap<>();

		properties.getUser().forEach((key, value) -> oauth2Provider.put(key,
			new OAuth2Provider(value, properties.getProvider().get(key))));
		return oauth2Provider;
	}
}
