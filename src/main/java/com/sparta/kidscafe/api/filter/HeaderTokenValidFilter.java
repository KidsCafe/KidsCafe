package com.sparta.kidscafe.api.filter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@Order(0)
@RequiredArgsConstructor
public class HeaderTokenValidFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		if(this.isApplicable(request)){
			filterChain.doFilter(request, response);
			return;
		}

		// Authorization 헤더 유무 검사
		String authorizationHeader = request.getHeader("Authorization");
		if (!StringUtils.hasText(authorizationHeader)) {
			filterChain.doFilter(request, response);
			return;
		}

		String accessToken = Optional.ofNullable(authorizationHeader)
			.filter(header -> header.startsWith("Bearer "))
			.map(header -> header.substring("Bearer ".length()))
			.orElse(null);

		if (accessToken == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효한 토큰이 존재하지 않습니다.");
		}

		filterChain.doFilter(request, response);
	}

	public boolean isApplicable(HttpServletRequest request) {
		// 회원 가입, 로그인 관련 API 는 인증 필요 없이 요청 진행
		String url = request.getRequestURI();
		String queryString = request.getQueryString();

		if (!StringUtils.hasText(url)) {
			return true;
		}

		if(url.equals("/") || url.equals("/index.html") || url.equals("/favicon.ico")){
			return true;
		}

		if (url.startsWith("/login/oauth/authorize") ||
			(url.startsWith("/redirect/oauth") && queryString != null && queryString.contains("code="))) {
			return true;
		}

		if (url.startsWith("/api/cafes/popular/db") ||
			url.startsWith("/api/cafes/popular/redis") ||
			url.startsWith("/api/popular/redis/update") ||
			url.startsWith("/api/cafes/popular/ranking") ||
			url.startsWith("/api/cafes/popular/ranking/update")) {
			return true;
		}

		return url.startsWith("/api/auth") ||
			url.startsWith("/api/oauth2") ||
			url.startsWith("/oauth2") ||
			url.startsWith("/error") ||
			// url.startsWith("/index.html") ||
			url.startsWith("/js") ||
			url.startsWith("/css");
	}
}
