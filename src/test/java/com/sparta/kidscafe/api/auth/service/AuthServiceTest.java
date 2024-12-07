package com.sparta.kidscafe.api.auth.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.naming.AuthenticationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sparta.kidscafe.api.auth.dto.SigninRequestDto;
import com.sparta.kidscafe.api.auth.dto.SignupRequestDto;
import com.sparta.kidscafe.common.util.JwtUtil;
import com.sparta.kidscafe.common.util.PasswordEncoder;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private AuthService authService;

	@Test
	@DisplayName("회원가입 : 중복 이메일 검증")
	public void duplicate_email_test(){
		// given
		SignupRequestDto signupRequestDto = new SignupRequestDto(
			"test@test.com",
			"1234",
			"test",
			"test si test gu test dong",
			"testNickname",
			"default");
		when(userRepository.existsByEmail(any())).thenReturn(true);

		// when // then
		assertThatThrownBy(() -> authService.signup(signupRequestDto))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("Email already in use : 이메일 중복입니다.");
	}
}