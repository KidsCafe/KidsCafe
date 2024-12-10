// package com.sparta.kidscafe.api.oauth2.controller;
//
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
//
// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.sparta.kidscafe.api.auth.controller.dto.SigninResponseDto;
// import com.sparta.kidscafe.api.oauth2.service.OAuth2Service;
//
// import lombok.RequiredArgsConstructor;
//
// @RestController
// @RequestMapping("/api/oauth2")
// @RequiredArgsConstructor
// public class OAuth2Controller {
//
// 	private final OAuth2Service oAuth2Service;
//
// 	@GetMapping("/sign/{provider}")
// 	public ResponseEntity<SigninResponseDto> signin(@PathVariable String provider, @RequestParam String code) throws
// 		JsonProcessingException {
// 		SigninResponseDto signinResponseDto = oAuth2Service.signinOrRegister(provider, code);
// 		return ResponseEntity
// 			.status(HttpStatus.OK)
// 			.body(signinResponseDto);
// 	}
// }
