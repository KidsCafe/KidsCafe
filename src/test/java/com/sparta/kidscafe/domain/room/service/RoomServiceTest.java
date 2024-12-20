package com.sparta.kidscafe.domain.room.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.room.dto.request.RoomCreateRequestDto;
import com.sparta.kidscafe.domain.room.dto.response.RoomResponseDto;
import com.sparta.kidscafe.domain.room.entity.Room;
import com.sparta.kidscafe.domain.room.repository.RoomRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static com.sparta.kidscafe.exception.ErrorCode.FORBIDDEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RoomServiceTest {

  @InjectMocks
  private RoomService roomService;

  @Mock
  private RoomRepository roomRepository;

  @Mock
  private CafeRepository cafeRepository;

  private AuthUser mockAuthUser;
  private Cafe mockCafe;
  private Room mockRoom;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    Long userId = 1L;
    mockAuthUser = new AuthUser(userId, "testUser", RoleType.OWNER);

    User mockUser = new User(userId, "testUser", RoleType.OWNER);
    mockCafe = Cafe.builder().id(1L).user(mockUser).build();

    mockRoom = mock(Room.class); // mock 객체로 변경
    when(mockRoom.getCafe()).thenReturn(mockCafe);
  }

  @Test
  void testCreateRoom_Success() {
    RoomCreateRequestDto request = mock(RoomCreateRequestDto.class);
    when(cafeRepository.findById(1L)).thenReturn(Optional.of(mockCafe));
    when(request.convertDtoToEntity(mockCafe)).thenReturn(mockRoom);

    StatusDto result = roomService.createRoom(mockAuthUser, request, 1L);

    verify(roomRepository, times(1)).save(mockRoom);
    assertEquals(HttpStatus.CREATED.value(), result.getStatus());
    assertEquals("룸 생성 성공", result.getMessage());
  }

  @Test
  void testCreateRoom_Forbidden() {
    Long userId = 2L;
    User mockUser = new User(userId, "testUser", RoleType.OWNER);
    mockCafe = Cafe.builder().id(1L).user(mockUser).build();

    RoomCreateRequestDto request = mock(RoomCreateRequestDto.class);
    when(cafeRepository.findById(1L)).thenReturn(Optional.of(mockCafe));

    BusinessException exception = assertThrows(BusinessException.class,
        () -> roomService.createRoom(mockAuthUser, request, 1L));

    assertEquals(FORBIDDEN, exception.getErrorCode());
    verify(roomRepository, never()).save(any());
  }

  @Test
  void testGetRooms_Success() {
    when(cafeRepository.findById(1L)).thenReturn(Optional.of(mockCafe));
    when(roomRepository.findAllByCafeId(1L)).thenReturn(List.of(mockRoom));

    ListResponseDto<RoomResponseDto> result = roomService.getRooms(1L);

    assertEquals(HttpStatus.OK.value(), result.getStatus());
    assertEquals("룸 조회 성공", result.getMessage());
    assertEquals(1, result.getData().size());
  }

  @Test
  void testUpdateRoom_Success() {
    RoomCreateRequestDto request = mock(RoomCreateRequestDto.class);
    when(roomRepository.findById(1L)).thenReturn(Optional.of(mockRoom));

    StatusDto result = roomService.updateRoom(mockAuthUser, 1L, request);

    verify(mockRoom, times(1)).updateRoom(request);
    assertEquals(HttpStatus.OK.value(), result.getStatus());
    assertEquals("룸 수정완료", result.getMessage());
  }

  @Test
  void testDeleteRoom_Success() {
    when(roomRepository.findById(1L)).thenReturn(Optional.of(mockRoom));

    roomService.deleteRoom(mockAuthUser, 1L);

    verify(roomRepository, times(1)).delete(mockRoom);
  }
}

