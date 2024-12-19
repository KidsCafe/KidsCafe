package com.sparta.kidscafe.domain.cafe.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.ResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.util.valid.AuthValidationCheck;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeCreateRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeSearchRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeSimpleRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.request.CafesSimpleCreateRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeDetailResponseDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeResponseDto;
import com.sparta.kidscafe.domain.cafe.service.CafeService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CafeController {

  private final CafeService cafeService;

  @PostMapping("/owners/cafes")
  public ResponseEntity<StatusDto> createCafe(
      @Auth AuthUser authUser,
      @Valid @RequestBody CafeCreateRequestDto requestDto
  ) {
    AuthValidationCheck.validOwner(authUser);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(cafeService.createCafe(authUser, requestDto));
  }

  @PostMapping("/admin/cafes")
  public ResponseEntity<StatusDto> createCafe(
      @Auth AuthUser authUser,
      @Valid @RequestBody CafesSimpleCreateRequestDto requestDto
  ) {
    AuthValidationCheck.validAdmin(authUser);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(cafeService.creatCafe(authUser, requestDto));
  }

  @GetMapping("/cafes")
  public ResponseEntity<PageResponseDto<CafeResponseDto>> searchCafe(
      @RequestBody CafeSearchRequestDto requestDto
  ) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(cafeService.searchCafe(requestDto.getSearchCondition()));
  }

  @GetMapping("owners/cafes")
  public ResponseEntity<PageResponseDto<CafeResponseDto>> searchCafeByOwner(
      @Auth AuthUser authUser,
      @RequestBody CafeSearchRequestDto requestDto
  ) {
    AuthValidationCheck.validOwner(authUser);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(cafeService.searchCafe(requestDto.getSearchCondition()));
  }

  @GetMapping("admin/cafes")
  public ResponseEntity<PageResponseDto<CafeResponseDto>> searchCafeByAdmin(
      @Auth AuthUser authUser,
      @RequestBody CafeSearchRequestDto requestDto
  ) {
    AuthValidationCheck.validAdmin(authUser);
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
      @Valid @RequestBody CafeSimpleRequestDto requestDto
  ) {
    AuthValidationCheck.validOwner(authUser);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(cafeService.updateCafe(authUser, cafeId, requestDto));
  }

  @DeleteMapping("owners/cafes/{cafeId}")
  public ResponseEntity<Void> deleteCafe(
      @Auth AuthUser authUser,
      @PathVariable Long cafeId
  ) {
    AuthValidationCheck.validOwner(authUser);
    cafeService.deleteCafe(authUser, cafeId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("admin/cafes")
  public ResponseEntity<Void> deleteCafe(
      @Auth AuthUser authUser,
      @RequestBody List<Long> cafes
  ) {
    AuthValidationCheck.validAdmin(authUser);
    cafeService.deleteCafe(authUser, cafes);
    return ResponseEntity.noContent().build();
  }
}
