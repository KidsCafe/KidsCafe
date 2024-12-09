package com.sparta.kidscafe.common.config;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.sparta.kidscafe.api.auth.exception.AuthException;
import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.enums.RoleType;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;

public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter){
		boolean hasAuthAnnotation = parameter.getParameterAnnotation(Auth.class) != null;
		boolean isAuthUserType = parameter.getParameterType().equals(AuthUser.class);

		if(hasAuthAnnotation != isAuthUserType){
			throw new AuthException("@Auth 를 사용하세요");
		}

		return hasAuthAnnotation;
	}

	@Override
	public Object resolveArgument(
		@Nullable MethodParameter parameter,
		@Nullable ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		@Nullable WebDataBinderFactory binderFactory
	){
		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();

		Long userId = (Long)request.getAttribute("userId");
		String email = (String)request.getAttribute("email");
		RoleType roleType = RoleType.of((String) request.getAttribute("roleType"));

		return new AuthUser(userId, email, roleType);
	}



}
