package com.sparta.kidscafe.domain.cafe.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.ResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeCreateRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeModifyRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeSearchRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeDetailResponseDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeResponseDto;
import com.sparta.kidscafe.domain.cafe.service.CafeService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
      @Auth AuthUser authUser,
      @RequestPart List<MultipartFile> cafeImages,
      @Valid @RequestPart CafeCreateRequestDto requestDto
  ) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(cafeService.createCafe(authUser, requestDto, cafeImages));
  }

  @GetMapping("/cafes")
  public ResponseEntity<PageResponseDto<CafeResponseDto>> searchCafe(
      @Valid @RequestBody CafeSearchRequestDto requestDto
  ) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(cafeService.searchCafe(requestDto.getSearchCondition()));
  }

  @GetMapping("/cafes/{cafeId}")
  public ResponseEntity<ResponseDto<CafeDetailResponseDto>> findCafe(
      @PathVariable Long cafeId
  ) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(cafeService.findCafe(cafeId));
  }

  @PatchMapping("/cafes/{cafeId}")
  public ResponseEntity<StatusDto> updateCafe(
      @Auth AuthUser authUser,
      @PathVariable Long cafeId,
      @RequestPart List<MultipartFile> cafeImages,
      @Valid @RequestPart CafeModifyRequestDto requestDto
      ) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(cafeService.updateCafe(authUser, cafeId, cafeImages, requestDto));
  }
}
