package com.sparta.kidscafe.domain.room.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.review.service.ReviewService;
import com.sparta.kidscafe.domain.room.dto.request.RoomCreateRequestDto;
import com.sparta.kidscafe.domain.room.dto.response.RoomResponseDto;
import com.sparta.kidscafe.domain.room.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoomController {

  private final RoomService roomService;
  private final ReviewService reviewService;

  @PostMapping("/cafes/{cafeId}/rooms")
  public ResponseEntity<StatusDto> createRoom(
      @Auth AuthUser authUser,
      @Valid @RequestBody RoomCreateRequestDto request,
      @PathVariable("cafeId") Long cafeId
  ) {
    StatusDto response = roomService.createRoom(authUser, request, cafeId);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/cafes/{cafeId}/rooms")
  public ResponseEntity<ListResponseDto<RoomResponseDto>> getRooms(
      @PathVariable("cafeId") Long cafeId
  ) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(roomService.getRooms(cafeId));
  }

  @PatchMapping("/cafes/rooms/{roomId}")
  public ResponseEntity<StatusDto> updateRoom(
      @Auth AuthUser authUser,
      @PathVariable("roomId") Long roomId,
      @Valid @RequestBody RoomCreateRequestDto request
  ) {
    StatusDto response = roomService.updateRoom(authUser, roomId, request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/cafes/rooms/{roomId}")
  public ResponseEntity<Void> deleteRoom(
      @Auth AuthUser authUser,
      @PathVariable("roomId") Long roomId
  ) {
    roomService.deleteRoom(authUser, roomId);
    return ResponseEntity.noContent().build();
  }
}
