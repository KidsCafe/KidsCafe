//package com.sparta.kidscafe.domain.reservation.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.sparta.kidscafe.common.dto.AuthUser;
//import com.sparta.kidscafe.common.dto.StatusDto;
//import com.sparta.kidscafe.common.enums.RoleType;
//import com.sparta.kidscafe.domain.cafe.entity.Cafe;
//import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
//import com.sparta.kidscafe.domain.fee.repository.FeeRepository;
//import com.sparta.kidscafe.domain.pricepolicy.repository.PricePolicyRepository;
//import com.sparta.kidscafe.domain.reservation.dto.request.ReservationCreateRequestDto;
//import com.sparta.kidscafe.domain.reservation.entity.Reservation;
//import com.sparta.kidscafe.domain.reservation.entity.ReservationDetail;
//import com.sparta.kidscafe.domain.reservation.enums.ReservationStatus;
//import com.sparta.kidscafe.domain.reservation.repository.ReservationDetailRepository;
//import com.sparta.kidscafe.domain.reservation.repository.ReservationRepository;
//import com.sparta.kidscafe.domain.room.repository.RoomRepository;
//import com.sparta.kidscafe.domain.user.entity.User;
//import com.sparta.kidscafe.domain.user.repository.UserRepository;
//import com.sparta.kidscafe.exception.BusinessException;
//import com.sparta.kidscafe.exception.ErrorCode;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.domain.Sort.Direction;
//import org.springframework.http.HttpStatus;
//
//@ExtendWith(MockitoExtension.class)
//public class ReservationServiceTest {
//
//  @InjectMocks
//  private ReservationService reservationService;
//  @Mock
//  private ReservationRepository reservationRepository;
//  @Mock
//  private CafeRepository cafeRepository;
//  @Mock
//  private RoomRepository roomRepository;
//  @Mock
//  private FeeRepository feeRepository;
//  @Mock
//  private ReservationDetailRepository reservationDetailRepository;
//  @Mock
//  private ReservationCalculationService reservationCalculationService;
//  @Mock
//  private UserRepository userRepository;
//
//  private AuthUser mockUser;
//  private AuthUser mockOwner;
//  private Pageable pageable;
//  private Reservation mockReservation;
//  private User testUser;
//  private Cafe testCafe;
//  private List<ReservationDetail> mockDetails;
//
//  @BeforeEach
//  void setUp() {
//    mockUser = new AuthUser(1L, RoleType.USER);
//    mockOwner = new AuthUser(2L, RoleType.OWNER);
//    pageable = PageRequest.of(0, 10, Sort.by(Direction.DESC, "createdAt"));
//
//    testUser = User.builder()
//        .id(1L)
//        .name("Test User")
//        .build();
//
//    testCafe = Cafe.builder()
//        .id(1L)
//        .name("Test Cafe")
//        .build();
//
//    mockReservation = Reservation.builder()
//        .id(1L)
//        .cafe(testCafe)
//        .user(testUser)
//        .startedAt(LocalDateTime.of(2024, 1, 1, 0, 0))
//        .finishedAt(LocalDateTime.of(2024, 1, 2, 0, 0))
//        .status(ReservationStatus.PENDING)
//        .totalPrice(1000)
//        .build();
//
//    mockDetails = List.of(mock(ReservationDetail.class));
//  }
//
//  @Test
//  @DisplayName("예약 생성: 성공")
//  void createReservation_success() {
//    // given
//    AuthUser authUser = new AuthUser(1L, RoleType.USER);
//    Long cafeId = 1L;
//    ReservationCreateRequestDto requestDto = mock(ReservationCreateRequestDto.class);
//
//    User user = new User();
//    Cafe cafe = new Cafe();
//    Reservation reservation = mock(Reservation.class);
//    List<ReservationDetail> details = List.of(mock(ReservationDetail.class));
//
//    when(userRepository.findById(authUser.getId())).thenReturn(Optional.of(user));
//    when(cafeRepository.findById(cafeId)).thenReturn(Optional.of(cafe));
//    when(reservationRepository.isRoomAvailable(any())).thenReturn(true);
//    when(requestDto.convertDtoToEntity(cafe, user)).thenReturn(reservation);
//    when(requestDto.convertEntityToDtoByReservationDetail(reservation)).thenReturn(details);
//
//    // when
//    StatusDto statusDto = reservationService.createReservation(authUser, cafeId, requestDto);
//
//    // then
//    verify(reservationCalculationService).calcReservation(reservation, details);
//    verify(reservationRepository).save(reservation);
//    verify(reservationDetailRepository).saveAll(details);
//
//    assertEquals(HttpStatus.CREATED.value(), statusDto.getStatus());
//    assertEquals("예약 완료", statusDto.getMessage());
//  }
//
//  @Test
//  @DisplayName("예약 생성: 실패 - 예약 가능한 룸이 없을 때")
//  void createReservation_roomNotAvailable() {
//    // given
//    AuthUser authUser = new AuthUser(1L, RoleType.USER);
//    Long cafeId = 1L;
//    ReservationCreateRequestDto requestDto = mock(ReservationCreateRequestDto.class);
//
//    User user = new User();
//    Cafe cafe = new Cafe();
//
//    when(userRepository.findById(authUser.getId())).thenReturn(Optional.of(user));
//    when(cafeRepository.findById(cafeId)).thenReturn(Optional.of(cafe));
//    when(reservationRepository.isRoomAvailable(any())).thenReturn(false);
//
//    // when & then
//    BusinessException exception = assertThrows(BusinessException.class, () ->
//        reservationService.createReservation(authUser, cafeId, requestDto));
//
//    assertEquals(ErrorCode.RESERVATION_FAILURE, exception.getErrorCode());
//  }
//
//
//}
