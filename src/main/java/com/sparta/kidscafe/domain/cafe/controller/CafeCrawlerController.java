package com.sparta.kidscafe.domain.cafe.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.util.valid.AuthValidationCheck;
import com.sparta.kidscafe.domain.cafe.service.CafeCrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CafeCrawlerController {

  private final CafeCrawlerService cafeCrawlerService;

  @PostMapping("admin/cafes/crawling")
  public ResponseEntity<StatusDto> crawlingCafe(
      @Auth AuthUser authUser
  ) {
    AuthValidationCheck.validAdmin(authUser);
    cafeCrawlerService.crawlingCafe();
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(StatusDto.createStatusDto(HttpStatus.CREATED, "크롤링 완료"));
  }
}