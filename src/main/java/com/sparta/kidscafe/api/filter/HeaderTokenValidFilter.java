package com.sparta.kidscafe.api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

@Component
@Order(0)
@RequiredArgsConstructor
public class HeaderTokenValidFilter extends OncePerRequestFilter {

	private final AntPathMatcher pathMatcher = new AntPathMatcher();

	// role -> PUBLIC 경로는 토큰 검증 거치지 않고 필터 통과
	private static final List<String> PUBLIC_API_PATHS = Arrays.asList(
			"/api/auth/**",
			"/api/oauth2/**",
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
			"/",
			"/index.html",
			"/favicon.ico",
			"/error/**",
			"/js/**",
			"/css/**"
	);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

		if(isApplicable(request)){
			filterChain.doFilter(request, response);
			return;
		}

		// Authorization 헤더 검증
		String authorizationHeader = request.getHeader("Authorization");
		if (!StringUtils.hasText(authorizationHeader)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, " Authorization 헤더가 없습니다.");
		}

		// Bearer 토큰 검증
		if (!authorizationHeader.startsWith("Bearer ")){
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰 형식입니다.");
		}

		// Bearer 토큰 추출 및 검증
		String accessToken = authorizationHeader.substring("Bearer ".length());
		if (!StringUtils.hasText(accessToken)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효한 토큰이 존재하지 않습니다.");
		}

		filterChain.doFilter(request, response);
	}

	public boolean isApplicable(HttpServletRequest request) {

		String url = request.getRequestURI();
		String queryString = request.getQueryString();

		// url 이 없으면 PUBLIC 아님
		if(!StringUtils.hasText(url)){
			return false;
		}

		// 쿼리스트링 포한된 api url 경로
		if(url.startsWith("/redirect/oauth") && queryString != null && queryString.contains("code=")){
			return true;
		}

    return PUBLIC_API_PATHS.stream().anyMatch(pattern -> pathMatcher.match(pattern, url));
	}
}
