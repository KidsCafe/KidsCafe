package com.sparta.kidscafe.domain.room.service;

import com.sparta.kidscafe.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {
  private final RoomRepository roomRepository;
}
