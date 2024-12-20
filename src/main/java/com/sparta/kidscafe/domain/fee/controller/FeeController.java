package com.sparta.kidscafe.domain.fee.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.fee.dto.request.FeeCreateRequestDto;
import com.sparta.kidscafe.domain.fee.dto.request.FeeUpdateRequestDto;
import com.sparta.kidscafe.domain.fee.dto.response.FeeResponseDto;
import com.sparta.kidscafe.domain.fee.service.FeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cafes")
public class FeeController {

  private final FeeService feeService;

  @PostMapping("/{cafeId}/fees")
  public ResponseEntity<StatusDto> createFee(
      @Auth AuthUser authUser,
      @PathVariable Long cafeId,
      @Valid @RequestBody FeeCreateRequestDto feeCreateRequestDto) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(feeService.createFee(authUser, cafeId, feeCreateRequestDto));
  }

  @GetMapping("/{cafeId}/fees")
  public ResponseEntity<ListResponseDto<FeeResponseDto>> getFeesByCafe(@PathVariable Long cafeId) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(feeService.getFeesByCafe(cafeId));
  }

  @PatchMapping("/{cafeId}/fees/{feeId}")
  public ResponseEntity<StatusDto> updateFee(
      @Auth AuthUser authUser,
      @PathVariable Long cafeId,
      @PathVariable Long feeId,
      @Valid @RequestBody FeeUpdateRequestDto feeUpdateRequestDto) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(feeService.updateFee(authUser, cafeId, feeId, feeUpdateRequestDto));
  }
}
