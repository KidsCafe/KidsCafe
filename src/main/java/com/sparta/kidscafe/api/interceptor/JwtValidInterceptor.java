package com.sparta.kidscafe.api.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.sparta.kidscafe.common.util.JwtUtil;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtValidInterceptor implements HandlerInterceptor {

	private final JwtUtil jwtUtil;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws RuntimeException {
		try{
			String accessToken = request.getHeader("Authorization");
			jwtUtil.validate(accessToken.substring("Bearer ".length()));
			return true;
		} catch(JwtException e){
			throw new JwtException(e.getMessage());
		}
	}
}
