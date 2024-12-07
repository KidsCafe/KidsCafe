package com.sparta.kidscafe.api.auth.service;

import org.springframework.stereotype.Service;

import com.sparta.kidscafe.api.auth.dto.SigninRequestDto;
import com.sparta.kidscafe.api.auth.dto.SigninResponseDto;
import com.sparta.kidscafe.api.auth.dto.SignupRequestDto;
import com.sparta.kidscafe.common.util.PasswordEncoder;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupRequestDto requestDto) {
        if(userRepository.existsByEmail(requestDto.email())){
            throw new IllegalArgumentException("Email already in use : 이메일 중복입니다.");
        }
        User user = User.builder()
            .email(requestDto.email())
            .password(passwordEncoder.encode(requestDto.password()))
            .name(requestDto.name())
            .nickname(requestDto.nickname())
            .address(requestDto.address())
            .build();
        userRepository.save(user);
    }

    public SigninResponseDto signin(SigninRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.email())
            .orElseThrow(() -> new IllegalArgumentException("Not Found Email : 등록된 이메일을 찾을 수 없습니다."));
        if(!passwordEncoder.matches(requestDto.password(), user.getPassword())){
            throw new IllegalArgumentException("Wrong password : 비밀번호가 일치하지 않습니다.");
        }
        return new SigninResponseDto(null);
    }
}
