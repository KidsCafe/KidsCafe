package com.sparta.kidscafe.domain.room.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.room.dto.request.RoomCreateRequestDto;
import com.sparta.kidscafe.domain.room.dto.response.RoomResponseDto;
import com.sparta.kidscafe.domain.room.entity.Room;
import com.sparta.kidscafe.domain.room.repository.RoomRepository;
import com.sparta.kidscafe.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sparta.kidscafe.exception.ErrorCode.*;

@Transactional
@Service
@RequiredArgsConstructor
public class RoomService {

  private final RoomRepository roomRepository;
  private final CafeRepository cafeRepository;

  public StatusDto createRoom(AuthUser authUser, RoomCreateRequestDto request, Long cafeId) {

    Long id = authUser.getId();

    Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new BusinessException(CAFE_NOT_FOUND));

    if (!id.equals(cafe.getUser().getId())) {
      throw new BusinessException(FORBIDDEN);
    }

    Room newRoom = request.convertDtoToEntity(cafe);

    roomRepository.save(newRoom);

    return StatusDto.builder()
        .status(HttpStatus.CREATED.value())
        .message("룸 생성 성공")
        .build();
  }

  @Transactional(readOnly = true)
  public ListResponseDto<RoomResponseDto> getRooms(Long cafeId) {

    cafeRepository.findById(cafeId).orElseThrow(() -> new BusinessException(CAFE_NOT_FOUND));

    List<Room> roomList = roomRepository.findAllByCafeId(cafeId);

    return ListResponseDto.success(roomList.stream()
            .map(RoomResponseDto::from)
            .toList(),
        HttpStatus.OK,
        "룸 조회 성공");
  }

  public StatusDto updateRoom(AuthUser authUser, Long roomId, RoomCreateRequestDto request) {

    Long id = authUser.getId();

    Room room = roomRepository.findById(roomId).orElseThrow(() -> new BusinessException(ROOM_NOT_FOUND));

    //캐싱적용 가능 부분
    if (!id.equals(room.getCafe().getUser().getId())) {
      throw new BusinessException(FORBIDDEN);
    }

    room.updateRoom(request);

    return StatusDto.builder()
        .status(HttpStatus.OK.value())
        .message("룸 수정완료")
        .build();
  }

  public void deleteRoom(AuthUser authUser, Long roomId) {

    Long id = authUser.getId();

    Room room = roomRepository.findById(roomId).orElseThrow(() -> new BusinessException(ROOM_NOT_FOUND));

    //캐싱적용 가능 부분
    if (!id.equals(room.getCafe().getUser().getId())) {
      throw new BusinessException(FORBIDDEN);
    }

    roomRepository.delete(room);
  }
}
