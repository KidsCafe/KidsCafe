package com.sparta.kidscafe.domain.room.controller;

import com.sparta.kidscafe.domain.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoomController {
  private final RoomService roomService;
}
