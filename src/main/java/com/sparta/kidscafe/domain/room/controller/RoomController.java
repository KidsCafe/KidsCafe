package com.sparta.kidscafe.domain.room.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.room.dto.request.RoomCreateRequestDto;
import com.sparta.kidscafe.domain.room.dto.response.RoomResponseDto;
import com.sparta.kidscafe.domain.room.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api")
public class RoomController {

  private final RoomService roomService;

  @PostMapping("/cafes/{cafeId}/rooms")
  public ResponseEntity<StatusDto> createRoom (
      @Auth AuthUser authUser,
      @Valid @RequestBody RoomCreateRequestDto request,
      @PathVariable Long cafeId
  ) {
    StatusDto response = roomService.createRoom(authUser, request, cafeId);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/cafes/{cafeid}/rooms")
  public ResponseEntity<ListResponseDto<RoomResponseDto>> getRooms(
      @PathVariable Long cafeId
  ) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(roomService.getRooms(cafeId));
  }

  @PatchMapping("/cafes/rooms/{roomId}")
  public ResponseEntity<StatusDto> updateRoom (
      @Auth AuthUser authUser,
      @PathVariable Long roomId,
      @Valid @RequestBody RoomCreateRequestDto request
  ) {
    StatusDto response = roomService.updateRoom(authUser, roomId, request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
