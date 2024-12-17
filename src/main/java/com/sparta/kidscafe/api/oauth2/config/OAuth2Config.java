package com.sparta.kidscafe.api.oauth2.config;

import java.util.Map;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sparta.kidscafe.api.oauth2.adapter.OAuth2Adapter;
import com.sparta.kidscafe.api.oauth2.provider.OAuth2Provider;

import lombok.RequiredArgsConstructor;

// 바인딩한 (OAuth2Properties) 객체를 사용하기위한 설정 파일
// 프로퍼티 파일에 적어준 정보가 하나의 OAuth2Properties 객체로 생성됨.
// 후에 각 OAuth2 서버 정보로 나눠서 InMemory 저장소에 저장하고 사용 -> 저장하기 전에 OAuth2Properties 를 분해해서 사용 -> OAuth2Provider 에서 작업
@Configuration
@EnableConfigurationProperties(OAuth2Properties.class)		// OAuth2Properties 객체 사용하기 위해 설정
@RequiredArgsConstructor
public class OAuth2Config {

	private final OAuth2Properties properties;

	// 빈으로 등록된 OAuth2Properties 를 주입받아 OAuth2Adapter 를 사용해 OAuth2 서버 정보를 가진 OAuth2Provider 로 분해하여 InMemoryProviderRepository 에 저장
	@Bean
	public InMemoryProviderRepository inMemoryProviderRepository() {
		Map<String, OAuth2Provider> providers = OAuth2Adapter.getOAuth2Providers(properties);
		return new InMemoryProviderRepository(providers);
	}
}
