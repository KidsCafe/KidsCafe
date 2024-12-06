package com.sparta.kidscafe.api.auth.controller;

import com.sparta.kidscafe.api.auth.dto.SigninRequestDto;
import com.sparta.kidscafe.api.auth.dto.SigninResponseDto;
import com.sparta.kidscafe.api.auth.dto.SignupRequestDto;
import com.sparta.kidscafe.api.auth.service.AuthService;
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
    public ResponseEntity<Void> signup(@RequestBody SignupRequestDto requestDto){
        authService.signup(requestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/signin")
    public ResponseEntity<SigninResponseDto> signin(@RequestBody SigninRequestDto requestDto){
        SigninResponseDto responseDto = authService.signin(requestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }
}
