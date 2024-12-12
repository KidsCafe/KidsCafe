package com.sparta.kidscafe.api.auth.controller;

import com.sparta.kidscafe.api.auth.controller.dto.SigninRequestDto;
import com.sparta.kidscafe.api.auth.controller.dto.SigninResponseDto;
import com.sparta.kidscafe.api.auth.controller.dto.SignupRequestDto;
import com.sparta.kidscafe.api.auth.service.AuthService;
import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto requestDto){
        authService.signup(requestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("회원가입이 정상적으로 처리되었습니다.");
    }

    @PostMapping("/signin")
    public ResponseEntity<SigninResponseDto> signin(HttpServletResponse res, @Valid @RequestBody SigninRequestDto requestDto){
        SigninResponseDto responseDto = authService.signin(res, requestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    // AuthUser 정보 확인 api
    @GetMapping("/user-info")
    public ResponseEntity<AuthUser> getUserInfo(@Auth AuthUser authUser){
        return ResponseEntity.ok(authUser);
    }
}
