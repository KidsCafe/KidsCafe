package com.sparta.kidscafe.domain.reservation.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.common.enums.TargetType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.reservation.dto.request.ReservationCreateRequestDto;
import com.sparta.kidscafe.domain.reservation.entity.Reservation;
import com.sparta.kidscafe.domain.reservation.repository.ReservationRepository;
import com.sparta.kidscafe.domain.room.entity.Room;
import com.sparta.kidscafe.domain.room.repository.RoomRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

  private final ReservationRepository reservationRepository;
  private final RoomRepository roomRepository;
  private final ReservationCalculationService reservationCalculationService;
  private final CafeRepository cafeRepository;
  private final UserRepository userRepository;

  @Transactional
  public StatusDto createReservation(AuthUser authUser, Long cafeId,
      ReservationCreateRequestDto requestDto) {
    // 1. 유저 확인
    Long userId = authUser.getId();
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    if (!authUser.getRoleType().equals(RoleType.USER)) {
      throw new BusinessException(ErrorCode.BAD_REQUEST);
    }
    // 2. 카페 확인
    Cafe cafe = cafeRepository.findById(cafeId)
        .orElseThrow(() -> new BusinessException(ErrorCode.CAFE_NOT_FOUND));

    // 3. 룸 확인
    Room room = roomRepository.findById(requestDto.getDetails()
            .stream().filter(item -> item.getTargetType().equals(TargetType.ROOM)).findFirst()
            .get().getTargetId())
        .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));
    // 룸이 존재하지 않을 경우 예외 처리
    if (!cafe.getRooms().contains(room)) {
      throw new BusinessException(ErrorCode.BAD_REQUEST);
    }

    // 4. totalPrice 계산
    int totalPrice = reservationCalculationService.calculateTotalPrice(cafe, room,
        requestDto.getDetails());

    // 5. 예약 생성
    Reservation reservation = Reservation.builder()
        .cafe(cafe)
        .user(user)
        .startedAt(requestDto.getStartedAt())
        .finishedAt(requestDto.getFinishedAt())
        .totalPrice(totalPrice)
        .build();

    reservationRepository.save(reservation);
    return StatusDto.builder()
        .status(HttpStatus.CREATED.value())
        .message("예약 완료")
        .build();
  }
}
