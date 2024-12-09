package com.sparta.kidscafe.api.oauth2.service;

import org.springframework.web.client.RestTemplate;

public class RestTemplateOAuth2ClientService extends OAuth2ClientService {
	public RestTemplateOAuth2ClientService(RestTemplate restTemplate) {
		super();
	}
}
