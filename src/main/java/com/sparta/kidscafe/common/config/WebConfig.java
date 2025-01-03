package com.sparta.kidscafe.common.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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
			.excludePathPatterns("/api/oauth2/**", "/api/auth/**",
					"/api/cafes",
					"/api/cafes/{cafeId}",
					"/api/cafes/{cafeId}/fees",
					"/api/cafes/v1/search?keyword={keyword}",
					"/api/cafes/v1/search/keywords/top10",
					"/api/cafes/v2/trends?keyword={keyword}",
					"/api/cafes/v2/search/keywords/top10",
					"/api/cafes/v3/trends?keyword={keyword}",
					"/api/cafes/v3/search/keywords/top10",
					"/api/cafes/{cafeId}/rooms",
					"/api/cafes/{cafeId}/reviews",
					"/favicon.ico", "/error", "/index.html", "/",  "/redirect/oauth/**");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // 모든 경로 허용
				.allowedOrigins("http://localhost:3000", "http://localhost:8080") // 허용할 출처
				.allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드
				.allowedHeaders("*") // 모든 헤더 허용
				.allowCredentials(true); // 인증 정보 허용
	}
}
