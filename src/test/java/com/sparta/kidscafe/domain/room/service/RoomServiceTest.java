package com.sparta.kidscafe.domain.room.service;

import static com.sparta.kidscafe.exception.ErrorCode.FORBIDDEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

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
  private AutoCloseable closeable; // AutoCloseable 선언

  @BeforeEach
  void setUp() {
    closeable = MockitoAnnotations.openMocks(this); // try-with-resources에 사용될 AutoCloseable 설정

    Long userId = 1L;
    mockAuthUser = new AuthUser(userId, "testUser", RoleType.OWNER);

    User mockUser = User.builder()
        .id(userId)
        .nickname("testUser")
        .role(RoleType.OWNER)
        .build();
    mockCafe = Cafe.builder()
        .id(1L)
        .user(mockUser)
        .build();

    mockRoom = mock(Room.class);
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
    User mockUser = User.builder()
        .id(userId)
        .nickname("testUser")
        .role(RoleType.OWNER)
        .build();
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
    // given
    RoomCreateRequestDto request = mock(RoomCreateRequestDto.class);
    when(request.getName()).thenReturn("Updated Room Name");
    when(request.getDescription()).thenReturn("Updated Description");
    when(request.getMinCount()).thenReturn(2);
    when(request.getMaxCount()).thenReturn(10);
    when(request.getPrice()).thenReturn(5000);

    when(roomRepository.findById(1L)).thenReturn(Optional.of(mockRoom));

    // when
    StatusDto result = roomService.updateRoom(mockAuthUser, 1L, request);

    // then
    verify(mockRoom, times(1)).updateRoom(
        "Updated Room Name",
        "Updated Description",
        2,
        10,
        5000
    ); // 엔티티의 메서드와 동일한 인자를 전달
    assertEquals(HttpStatus.OK.value(), result.getStatus());
    assertEquals("룸 수정 완료", result.getMessage());
  }

  @Test
  void testDeleteRoom_Success() {
    when(roomRepository.findById(1L)).thenReturn(Optional.of(mockRoom));

    roomService.deleteRoom(mockAuthUser, 1L);

    verify(roomRepository, times(1)).delete(mockRoom);
  }

  // AfterEach 추가 AutoCloseable 닫기
  @AfterEach
  void tearDown() throws Exception {
    closeable.close(); // openMocks로 연 AutoCloseable 자원 정리
  }
}
