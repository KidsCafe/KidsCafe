package com.sparta.kidscafe.api.auth.controller.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.common.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

	private final JwtUtil jwtUtil;

	@Override
	public boolean supportsParameter(MethodParameter parameter){
		return parameter.getParameterAnnotation(Auth.class) != null && parameter.getParameterType().equals(AuthUser.class);
	}

	@Override
	public Object resolveArgument(
		MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory
	) {
		String authorizationHeader = webRequest.getHeader("Authorization");

		if(authorizationHeader == null){
			throw new IllegalArgumentException("유효하지 않은 Authorization 헤더입니다.");
		}

		String accessToken;
		if(authorizationHeader.startsWith("Bearer ")){
			accessToken = authorizationHeader.substring("Bearer ".length());
		} else {
			accessToken = authorizationHeader;
		}

		Long userId = jwtUtil.extractUserId(accessToken);
		String email = jwtUtil.extractEmail(accessToken);
		RoleType roleType = RoleType.valueOf(jwtUtil.extractRoleType(accessToken));

		return new AuthUser(userId, email, roleType);
	}
}
