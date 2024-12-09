package com.sparta.kidscafe.api.oauth2.config;

import java.util.Map;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.sparta.kidscafe.api.oauth2.adapter.OAuth2Adapter;
import com.sparta.kidscafe.api.oauth2.provider.OAuth2ProviderProperties;
import com.sparta.kidscafe.api.oauth2.service.OAuth2ClientService;
import com.sparta.kidscafe.api.oauth2.service.OAuth2ProviderType;
import com.sparta.kidscafe.api.oauth2.service.RestTemplateOAuth2ClientService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableConfigurationProperties(OAuth2Properties.class)
@RequiredArgsConstructor
public class OAuth2Config {

	private final OAuth2Properties properties;
	private final RestTemplate restTemplate;
	private final OAuth2ClientService oAuth2ClientService;

	@Bean
	public InMemoryProviderRepository inMemoryProviderRepository(){
		Map<OAuth2ProviderType, OAuth2ProviderProperties> provider = OAuth2Adapter.getOAuth2Provider(properties);
		return new InMemoryProviderRepository(provider);
	}

	@Bean
	public OAuth2ClientService oAuth2ClientService(){
		return new RestTemplateOAuth2ClientService(restTemplate);
	}
}
