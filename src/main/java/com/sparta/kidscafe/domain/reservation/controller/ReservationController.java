package com.sparta.kidscafe.domain.reservation.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.reservation.dto.request.ReservationCreateRequestDto;
import com.sparta.kidscafe.domain.reservation.dto.response.ReservationResponseDto;
import com.sparta.kidscafe.domain.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReservationController {

  private final ReservationService reservationService;

  @PostMapping("/temp/reservations/cafes/{cafeId}")
  public ResponseEntity<StatusDto> tempCreateReservation(
      @Auth AuthUser authUser,
      @PathVariable Long cafeId,
      @Valid @RequestBody ReservationCreateRequestDto requestDto) {
    StatusDto response = reservationService.createReservation(authUser, cafeId, requestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // 예약 생성
  @PostMapping("/reservations/cafes/{cafeId}")
  public ResponseEntity<StatusDto> createReservation(
      @Auth AuthUser authUser,
      @PathVariable Long cafeId,
      @Valid @RequestBody ReservationCreateRequestDto requestDto) {
    StatusDto response = reservationService.tempCreateReservation(authUser, cafeId, requestDto);
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

  // 예약 승인
  @PutMapping("owners/reservations/{reservationId}/approve")
  public ResponseEntity<StatusDto> approveReservation(
      @Auth AuthUser authUser,
      @PathVariable Long reservationId) {
    StatusDto response = reservationService.approveReservation(authUser, reservationId);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }


}
