package com.sparta.kidscafe.common.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sparta.kidscafe.api.auth.controller.resolver.AuthUserArgumentResolver;
import com.sparta.kidscafe.api.interceptor.JwtValidInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final AuthUserArgumentResolver authUserArgumentResolver;
	private final JwtValidInterceptor jwtValidInterceptor;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers){
		resolvers.add(authUserArgumentResolver);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtValidInterceptor)
			.excludePathPatterns("/api/oauth2/**", "/api/auth/**", "/favicon.ico", "/error", "/oauth2.html");
	}
}
