package com.sparta.kidscafe.api.auth.controller.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.enums.LoginType;
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

		if(authorizationHeader == null || authorizationHeader.isBlank()){
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 Authorization 헤더입니다.");
		}

		String accessToken = extractToken(authorizationHeader);

		Long userId = jwtUtil.extractUserId(accessToken);
		String email = jwtUtil.extractEmail(accessToken);
		RoleType roleType = RoleType.valueOf(jwtUtil.extractRoleType(accessToken));
		LoginType loginType = LoginType.valueOf(jwtUtil.extractLoginType(accessToken));

		if (!isAuthorized(roleType)){
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");
		}

		// 일반 로그인 사용자
		if (loginType == LoginType.BASIC) {
			return new AuthUser(userId, email, roleType);
		}

		// 소셜 로그인 사용자 ( 수정 후 추가 예정 )
		// if (loginType == LoginType.){
		//
		// }

		if (roleType == RoleType.ADMIN || roleType == RoleType.USER || roleType == RoleType.OWNER) {
			return new AuthUser(userId, email, roleType);
		}

		throw new IllegalArgumentException("유효하지 않은 LoginType 입니다.");
	}

	private boolean isAuthorized(RoleType roleType){
		return RoleType.ADMIN.equals(roleType) || RoleType.OWNER.equals(roleType);
	}

	private String extractToken(String authorizationHeader) {
		if (authorizationHeader.startsWith("Bearer ")) {
			return authorizationHeader.substring("Bearer ".length());
		}
		return authorizationHeader;
	}
}
