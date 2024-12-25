package com.sparta.kidscafe.api.oauth2.controller;

import com.sparta.kidscafe.api.oauth2.controller.dto.OAuth2UserProfileDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.kidscafe.api.oauth2.controller.dto.OAuthSigninResponseDto;
import com.sparta.kidscafe.api.oauth2.service.OAuth2Service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

	private final OAuth2Service oAuth2Service;

	@GetMapping("/api/oauth2/{provider}/signin")
	public ResponseEntity<OAuthSigninResponseDto> signin(HttpServletResponse res, @PathVariable String provider, @RequestParam String code) throws JsonProcessingException {
		OAuthSigninResponseDto oAuthSigninResponseDto = oAuth2Service.signin(res, provider, code);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(oAuthSigninResponseDto);
	}

//	@PostMapping("/{provider}/signin")
//	public ResponseEntity<?> signinForKakao(HttpServletResponse res, @PathVariable String provider, @RequestHeader("Authorization") String authorizationHeader, @RequestParam String code){
//		String accessToken = authorizationHeader.replace("Bearer ", "");
//		OAuthSigninResponseDto oAuth2UserProfileDto = oAuth2Service.signin(res, provider, accessToken, code);
//		return ResponseEntity.ok(oAuth2UserProfileDto);
//	}

	@GetMapping("/redirect/oauth")
	public ResponseEntity<String> handleOauthRedirect(@RequestParam("code") String code){
		return ResponseEntity.ok("인증 코드 : " + code);
	}
}
