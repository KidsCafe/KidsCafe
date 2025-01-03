package com.sparta.kidscafe.domain.reservation.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.ResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.util.valid.AuthValidationCheck;
import com.sparta.kidscafe.domain.reservation.dto.request.ReservationCreateRequestDto;
import com.sparta.kidscafe.domain.reservation.dto.request.ReservationUpdateRequestDto;
import com.sparta.kidscafe.domain.reservation.dto.response.ReservationResponseDto;
import com.sparta.kidscafe.domain.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReservationController {

  private final ReservationService reservationService;

  // 예약 생성
  @PostMapping("/reservations/cafes/{cafeId}")
  public ResponseEntity<StatusDto> createReservation(
      @Auth AuthUser authUser,
      @PathVariable Long cafeId,
      @Valid @RequestBody ReservationCreateRequestDto requestDto) {
    AuthValidationCheck.validUser(authUser);
    StatusDto response = reservationService.createReservation(authUser, cafeId, requestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // 예약 내역 조회(User용)
  @GetMapping("/users/reservations")
  public ResponseEntity<PageResponseDto<ReservationResponseDto>> getReservationsByUser(
      @Auth AuthUser authUser,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    PageResponseDto<ReservationResponseDto> response = reservationService.getReservationsByUser(
        authUser, page, size);
    return ResponseEntity.ok(response);
  }

  // 예약 내역 조회(Owner용)
  @GetMapping("owners/reservations/cafes/{cafeId}")
  public ResponseEntity<PageResponseDto<ReservationResponseDto>> getReservationsByOwner(
      @Auth AuthUser authUser,
      @PathVariable Long cafeId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    PageResponseDto<ReservationResponseDto> response = reservationService.getReservationsByOwner(
        authUser, cafeId, page, size);
    return ResponseEntity.ok(response);
  }

  // 예약 상세 조회(User용)
  @GetMapping("/users/reservations/{reservationId}")
  public ResponseEntity<ResponseDto<ReservationResponseDto>> getReservationDetailByUser(
      @Auth AuthUser authUser,
      @PathVariable Long reservationId) {
    ResponseDto<ReservationResponseDto> response = reservationService.getReservationDetailByUser(
        authUser, reservationId);
    return ResponseEntity.ok(response);
  }

  // 예약 상세 조회(Owner용)
  @GetMapping("/owners/reservations/{reservationId}")
  public ResponseEntity<ResponseDto<ReservationResponseDto>> getReservationDetailByOwner(
      @Auth AuthUser authUser,
      @PathVariable Long reservationId) {
    ResponseDto<ReservationResponseDto> response =
        reservationService.getReservationDetailByOwner(authUser, reservationId);
    return ResponseEntity.ok(response);
  }

  // 예약 수정(User용)
  @PatchMapping("/users/reservations/{reservationId}/update")
  public ResponseEntity<StatusDto> updateReservation(
      @Auth AuthUser authUser,
      @PathVariable Long reservationId,
      @Valid @RequestBody ReservationUpdateRequestDto requestDto) {
    StatusDto response = reservationService.updateReservation(authUser, reservationId, requestDto);
    return ResponseEntity.ok(response);
  }

  // 예약 승인
  @PutMapping("owners/reservations/{reservationId}/approve")
  public ResponseEntity<StatusDto> approveReservation(
      @Auth AuthUser authUser,
      @PathVariable Long reservationId) {
    StatusDto response = reservationService.approveReservation(authUser, reservationId);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  // 예약 상태 변경
  @PutMapping("/owners/reservations/{reservationId}/complete")
  public ResponseEntity<StatusDto> completeReservation(
      @Auth AuthUser authUser,
      @PathVariable Long reservationId) {
    StatusDto response = reservationService.confirmPayment(authUser, reservationId);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  // 예약 취소: User용
  @PutMapping("/users/reservations/{reservationId}/cancel")
  public ResponseEntity<StatusDto> cancelReservationByUser(
      @Auth AuthUser authUser,
      @PathVariable Long reservationId) {
    StatusDto response = reservationService.cancelReservationByUser(authUser, reservationId);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  // 예약 취소: Owner용
  @PutMapping("/owners/reservations/{reservationId}/{cafeId}/cancel")
  public ResponseEntity<StatusDto> cancelReservationByOwner(
      @Auth AuthUser authUser,
      @PathVariable Long reservationId,
      @PathVariable Long cafeId) {
    StatusDto response = reservationService.cancelReservationByOwner(authUser, reservationId,
        cafeId);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
