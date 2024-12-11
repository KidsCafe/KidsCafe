package com.sparta.kidscafe.domain.fee.controller;

import java.util.List;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.domain.fee.dto.request.FeeCreateRequestDto;
import com.sparta.kidscafe.domain.fee.dto.request.FeeUpdateRequestDto;
import com.sparta.kidscafe.domain.fee.dto.response.FeeResponseDto;
import com.sparta.kidscafe.domain.fee.service.FeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cafes")
public class FeeController {

  private final FeeService feeService;

  @PostMapping("/{cafeId}/fees")
  public ResponseEntity<FeeResponseDto> createFee(
      @Auth AuthUser authUser,
      @PathVariable Long cafeId,
      @Valid @RequestBody FeeCreateRequestDto feeCreateRequestDto){
    FeeResponseDto feeResponseDto = feeService.createFee(authUser, cafeId, feeCreateRequestDto);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(feeResponseDto);
  }

  @GetMapping("/{cafeId}/fees")
  public ResponseEntity<List<FeeResponseDto>> getFeesByCafe(@PathVariable Long cafeId){
    List<FeeResponseDto> feeResponseDtoList = feeService.getFeesByCafe(cafeId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(feeResponseDtoList);
  }

  @PatchMapping("/{cafeId}/fees/{feeId}")
  public ResponseEntity<FeeResponseDto> updateFee(
      @Auth AuthUser authUser,
      @PathVariable Long cafeId,
      @PathVariable Long feeId,
      @Valid @RequestBody FeeUpdateRequestDto feeUpdateRequestDto){
    FeeResponseDto feeResponseDto = feeService.updateFee(authUser, cafeId, feeId, feeUpdateRequestDto);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(feeResponseDto);
  }
}
