package com.sparta.kidscafe.domain.cafe.controller;

import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeCreateRequestDto;
import com.sparta.kidscafe.domain.cafe.service.CafeService;
import com.sparta.kidscafe.domain.user.entity.User;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CafeController {

  private final CafeService cafeService;

  @PostMapping("/owners/cafes")
  public ResponseEntity<StatusDto> createCafe(
      // TODO khj [인증/인가] 완료되면 교체할 것.
      @Valid @RequestPart CafeCreateRequestDto requestDto,
      @RequestPart List<MultipartFile> cafeImages
  ) {
    // TODO khj [인증/인가] 완료되면 교체할 것.
    User user = User.builder().id(15L).build();
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(cafeService.createCafe(user, requestDto, cafeImages));
  }
}
