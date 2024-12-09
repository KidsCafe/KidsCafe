package com.sparta.kidscafe.api.auth.service;

import static com.sparta.kidscafe.exception.ErrorCode.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sparta.kidscafe.api.auth.controller.dto.SigninRequestDto;
import com.sparta.kidscafe.api.auth.controller.dto.SignupRequestDto;
import com.sparta.kidscafe.common.util.PasswordEncoder;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import com.sparta.kidscafe.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

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
			.isInstanceOf(BusinessException.class)
			.hasMessageContaining(String.valueOf(DUPLICATE_EMAIL));
	}

	@Test
	@DisplayName("회원가입 : 정상 회원가입")
	public void signup_test(){
		// given
		SignupRequestDto signupRequestDto = new SignupRequestDto(
			"test1@test.com",
			"1234",
			"test1",
			"test address",
			"testNickname1",
			"default"
		);
		when(userRepository.existsByEmail(any())).thenReturn(false);

		// when
		authService.signup(signupRequestDto);

		// then
		verify(userRepository, times(1)).save(any());
	}

	@Test
	@DisplayName("로그인 : 사용자를 찾을 수 없습니다.")
	public void do_not_exist_user_test(){
		// given
		SigninRequestDto signinRequestDto = new SigninRequestDto(
			"test@test.com",
			"1234"
		);
		when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

		// when // then
		assertThatThrownBy(() -> authService.signin(signinRequestDto))
			.isInstanceOf(BusinessException.class)
			.hasMessageContaining(String.valueOf(USER_NOT_FOUND));
	}

	// @Test
	// @DisplayName("로그인 : 비밀번호가 틀립니다.")
	// public
}