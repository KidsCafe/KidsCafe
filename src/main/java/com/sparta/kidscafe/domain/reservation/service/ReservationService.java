package com.sparta.kidscafe.domain.reservation.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.reservation.dto.request.ReservationCreateRequestDto;
import com.sparta.kidscafe.domain.reservation.dto.response.ReservationResponseDto;
import com.sparta.kidscafe.domain.reservation.entity.Reservation;
import com.sparta.kidscafe.domain.reservation.entity.ReservationDetail;
import com.sparta.kidscafe.domain.reservation.repository.ReservationDetailRepository;
import com.sparta.kidscafe.domain.reservation.repository.ReservationRepository;
import com.sparta.kidscafe.domain.reservation.repository.condition.ReservationSearchCondition;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

  private final UserRepository userRepository;
  private final CafeRepository cafeRepository;
  private final ReservationRepository reservationRepository;
  private final ReservationDetailRepository reservationDetailRepository;
  private final ReservationCalculationService reservationCalculationService;

  @Transactional
  public StatusDto createReservation(AuthUser authUser, Long cafeId,
      ReservationCreateRequestDto requestDto) {
    // 1. 유저 확인
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    // 2. 카페 확인
    Cafe cafe = cafeRepository.findById(cafeId)
        .orElseThrow(() -> new BusinessException(ErrorCode.CAFE_NOT_FOUND));

    // 3. 예약 가능한 상태인지 조회
    ReservationSearchCondition condition = requestDto.createSearchCondition(cafeId);
    if (!reservationRepository.isRoomAvailable(condition)) {
      throw new BusinessException(ErrorCode.RESERVATION_FAILURE);
    }

    //4. 저장
    saveReservations(user, cafe, requestDto);

    // 8. 반환
    return StatusDto.builder()
        .status(HttpStatus.CREATED.value())
        .message("예약 완료")
        .build();
  }

  public void saveReservations(User user, Cafe cafe, ReservationCreateRequestDto requestDto) {
    // 4. 예약이 가능하다면 예약 생성
    Reservation reservation = requestDto.convertDtoToEntity(cafe, user);
    List<ReservationDetail> reservationDetails =
        requestDto.convertEntityToDtoByReservationDetail(reservation);
    reservationCalculationService.calcReservation(cafe, user, reservation, reservationDetails);

    // 7. 저장
    reservationRepository.save(reservation);
    reservationDetailRepository.saveAll(reservationDetails);
  }

  // 예약 내역 조회(User용)
  @Transactional(readOnly = true)
  public PageResponseDto<ReservationResponseDto> getReservationsByUser(AuthUser authUser, int page,
      int size) {
    if (!authUser.getRoleType().equals(RoleType.USER)) {
      throw new BusinessException(ErrorCode.FORBIDDEN);
    }
    Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "createdAt"));
    Page<Reservation> reservationsForUser = reservationRepository.findByUserId(authUser.getId(),
        pageable);

    Page<ReservationResponseDto> responseDto = reservationsForUser.map(reservation ->
        ReservationResponseDto.builder()
            .reservationId(reservation.getId())
            .cafeId(reservation.getCafe().getId())
            .cafeName(reservation.getCafe().getName())
            .startedAt(reservation.getStartedAt())
            .finishedAt(reservation.getFinishedAt())
            .totalPrice(reservation.getTotalPrice())
            .build()
    );
    return PageResponseDto.success(responseDto, HttpStatus.OK, "예약 내역 조회(사용자용) 성공");
  }

  // 예약 내역 조회(Owner용)
  @Transactional(readOnly = true)
  public PageResponseDto<ReservationResponseDto> getReservationsByOwner(AuthUser authUser,
      Long cafeId, int page, int size) {
    if (!authUser.getRoleType().equals(RoleType.OWNER)) {
      throw new BusinessException(ErrorCode.FORBIDDEN);
    }
    Boolean isOwner = cafeRepository.existsByIdAndUserId(cafeId, authUser.getId());
    if (!isOwner) {
      throw new BusinessException(ErrorCode.FORBIDDEN);
    }
    Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "createdAt"));
    Page<Reservation> reservationsForOwner = reservationRepository.findByCafeId(cafeId, pageable);

    Page<ReservationResponseDto> responseDto = reservationsForOwner.map(reservation ->
        ReservationResponseDto.builder()
            .reservationId(reservation.getId())
            .userId(reservation.getUser().getId())
            .userName(reservation.getUser().getName())
            .cafeName(reservation.getCafe().getName())
            .startedAt(reservation.getStartedAt())
            .finishedAt(reservation.getFinishedAt())
            .totalPrice(reservation.getTotalPrice())
            .build()
    );
    return PageResponseDto.success(responseDto, HttpStatus.OK, "예약 내역 조회(카페용) 성공");
  }
}
