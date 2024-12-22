package com.sparta.kidscafe.domain.cafe.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.ResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.util.valid.AuthValidationCheck;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeSearchRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeSimpleRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.request.CafesSimpleRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeDetailResponseDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeSimpleResponseDto;
import com.sparta.kidscafe.domain.cafe.service.CafeService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
      @Valid @RequestBody CafeRequestDto requestDto
  ) {
    AuthValidationCheck.validOwner(authUser);
    cafeService.createCafe(authUser, requestDto);
    String message = "[" + requestDto.getName() + "] 등록 성공";
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(StatusDto.createStatusDto(HttpStatus.CREATED, message));
  }

  @PostMapping("/admin/cafes")
  public ResponseEntity<StatusDto> createCafe(
      @Auth AuthUser authUser,
      @Valid @RequestBody CafesSimpleRequestDto requestDto
  ) {
    AuthValidationCheck.validAdmin(authUser);
    cafeService.creatCafe(authUser, requestDto);
    String message = "카페 [" + requestDto.getCafes().size() + "]개 등록 성공";
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(StatusDto.createStatusDto(HttpStatus.CREATED, message));
  }

  @GetMapping("/cafes")
  public ResponseEntity<PageResponseDto<CafeSimpleResponseDto>> searchCafe(
      @RequestBody CafeSearchRequestDto requestDto
  ) {
    Page<CafeSimpleResponseDto> cafes = cafeService.searchCafe(requestDto.getSearchCondition());
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(PageResponseDto.create(cafes));
  }

  @GetMapping("/owners/cafes")
  public ResponseEntity<PageResponseDto<CafeSimpleResponseDto>> searchCafeByOwner(
      @Auth AuthUser authUser,
      @RequestBody CafeSearchRequestDto requestDto
  ) {
    AuthValidationCheck.validOwner(authUser);
    Page<CafeSimpleResponseDto> cafes = cafeService.searchCafe(requestDto.getSearchCondition());
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(PageResponseDto.create(cafes));
  }

  @GetMapping("/admin/cafes")
  public ResponseEntity<PageResponseDto<CafeSimpleResponseDto>> searchCafeByAdmin(
      @Auth AuthUser authUser,
      @RequestBody CafeSearchRequestDto requestDto
  ) {
    AuthValidationCheck.validAdmin(authUser);
    Page<CafeSimpleResponseDto> cafes = cafeService.searchCafe(requestDto.getSearchCondition());
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(PageResponseDto.create(cafes));
  }

  @GetMapping("/cafes/{cafeId}")
  public ResponseEntity<ResponseDto<CafeDetailResponseDto>> findCafe(
      @PathVariable Long cafeId
  ) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(ResponseDto.create(cafeService.findCafe(cafeId)));
  }

  @PatchMapping("/cafes/{cafeId}")
  public ResponseEntity<StatusDto> updateCafe(
      @Auth AuthUser authUser,
      @PathVariable Long cafeId,
      @Valid @RequestBody CafeSimpleRequestDto requestDto
  ) {
    AuthValidationCheck.validOwner(authUser);
    cafeService.updateCafe(authUser, cafeId, requestDto);
    String message = "[" + requestDto.getName() + "] 수정 성공";
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(StatusDto.createStatusDto(HttpStatus.OK, message));
  }

  @DeleteMapping("/owners/cafes/{cafeId}")
  public ResponseEntity<Void> deleteCafe(
      @Auth AuthUser authUser,
      @PathVariable Long cafeId
  ) {
    AuthValidationCheck.validOwner(authUser);
    cafeService.deleteCafe(authUser, cafeId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/admin/cafes")
  public ResponseEntity<Void> deleteCafe(
      @Auth AuthUser authUser,
      @RequestBody List<Long> cafes
  ) {
    AuthValidationCheck.validAdmin(authUser);
    cafeService.deleteCafe(authUser, cafes);
    return ResponseEntity.noContent().build();
  }
}
