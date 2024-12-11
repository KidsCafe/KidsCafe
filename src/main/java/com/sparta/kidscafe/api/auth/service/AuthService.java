package com.sparta.kidscafe.api.auth.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sparta.kidscafe.api.auth.controller.dto.SigninRequestDto;
import com.sparta.kidscafe.api.auth.controller.dto.SigninResponseDto;
import com.sparta.kidscafe.api.auth.controller.dto.SignupRequestDto;
import com.sparta.kidscafe.common.enums.LoginType;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.common.util.JwtUtil;
import com.sparta.kidscafe.common.util.PasswordEncoder;
import com.sparta.kidscafe.domain.user.entity.OAuthMember;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void signup(SignupRequestDto requestDto) {
        if(userRepository.existsByEmail(requestDto.email())){
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }
        User user = User.builder()
            .email(requestDto.email())
            .password(passwordEncoder.encode(requestDto.password()))
            .name(requestDto.name())
            .nickname(requestDto.nickname())
            .address(requestDto.address())
            .role(RoleType.valueOf(requestDto.role().toUpperCase()))
            .loginType(LoginType.valueOf(requestDto.loginType().toUpperCase()))
            .build();
        userRepository.save(user);
    }

    public SigninResponseDto signin(SigninRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.email())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if(!passwordEncoder.matches(requestDto.password(), user.getPassword())){
            throw new BusinessException(ErrorCode.WRONG_PASSWORD);
        }
        String accessToken = jwtUtil.generateAccessToken(user);
        System.out.println("token: " + accessToken);
        return new SigninResponseDto(accessToken);
    }

    // 1. 이메일이 등록된 이메일인지 확인 -> 등록되어 있으면 예외처리
    // 2. 등록되지 않은 이메일일 경우 -> signupRequestDto 생성
    // 3. signup 을 호출
    public void signupWithOAuth(String oauthId, String email, String name) {
        userRepository.findByEmail(email)
            .ifPresentOrElse(
                savedUser -> savedUser.udpateOAuthId(oauthId),
                () -> {
                    String password = passwordEncoder.encode(UUID.randomUUID().toString());
                    User oAuth2User = User.builder()
                        .oauthId(oauthId)
                        .email(email)
                        .nickname(name)
                        .password(password)
                        .loginType(LoginType.OAUTH)
                        .role(RoleType.USER)
                        .build();
                    userRepository.save(oAuth2User);
                }
            );
    }

    public SigninResponseDto signinWithOAuth(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        String accessToken = jwtUtil.generateAccessToken(user);
        return new SigninResponseDto(accessToken);
    }
}
