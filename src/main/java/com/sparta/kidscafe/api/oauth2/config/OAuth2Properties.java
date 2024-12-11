package com.sparta.kidscafe.api.oauth2.config;

import java.security.Provider;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.sparta.kidscafe.domain.user.entity.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@ConfigurationProperties(prefix = "oauth2")
public class OAuth2Properties {

	private final Map<String, User> user = new HashMap<>();
	private final Map<String, Provider> provider = new HashMap<>();

	@Getter
	@Setter
	public static class User {
		private String clientId;
		private String clientSecret;
		private String redirectUri;
	}

	@Getter
	@Setter
	public static class Provider{
		private String tokenUri;
		private String userInfoUri;
		private String userNameAttribute;
	}
}
