package com.sparta.kidscafe.api.filter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
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

		String accessToken = Optional.ofNullable(request.getHeader("Authorization"))
			.map(header -> header.substring("Bearer ".length()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."));
		filterChain.doFilter(request, response);
	}

	public boolean isApplicable(HttpServletRequest request) {
		return request.getRequestURI().startsWith("/api/auth");
	}
}