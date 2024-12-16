package com.sparta.kidscafe.domain.reservation.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.pricepolicy.repository.PricePolicyRepository;
import com.sparta.kidscafe.domain.reservation.dto.request.ReservationCreateRequestDto;
import com.sparta.kidscafe.domain.reservation.dto.response.ReservationResponseDto;
import com.sparta.kidscafe.domain.reservation.entity.Reservation;
import com.sparta.kidscafe.domain.reservation.entity.ReservationDetail;
import com.sparta.kidscafe.domain.reservation.enums.ReservationStatus;
import com.sparta.kidscafe.domain.reservation.repository.ReservationDetailRepository;
import com.sparta.kidscafe.domain.reservation.repository.ReservationRepository;
import com.sparta.kidscafe.domain.reservation.repository.condition.ReservationSearchCondition;
import com.sparta.kidscafe.domain.room.repository.RoomRepository;
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

  private final ReservationRepository reservationRepository;
  private final RoomRepository roomRepository;
  private final ReservationCalculationService reservationCalculationService;
  private final CafeRepository cafeRepository;
  private final UserRepository userRepository;
  private final PricePolicyRepository pricePolicyRepository;
  private final ReservationDetailRepository reservationDetailRepository;

  @Transactional
  public StatusDto createReservation(AuthUser authUser, Long cafeId,
      ReservationCreateRequestDto requestDto) {
    // 1. 유저 확인
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    if (!authUser.getRoleType().equals(RoleType.USER)) {
      throw new BusinessException(ErrorCode.BAD_REQUEST);
    }
    // 2. 카페 확인
    Cafe cafe = cafeRepository.findById(cafeId)
        .orElseThrow(() -> new BusinessException(ErrorCode.CAFE_NOT_FOUND));
    // 3. 예약 가능한 상태인지 조회
    ReservationSearchCondition condition = requestDto.createSearchCondition(cafeId);
    if (!reservationRepository.isRoomAvailable(condition)) {
      throw new BusinessException(ErrorCode.RESERVATION_FAILURE);
    }
    // 4. 예약
    saveReservations(user, cafe, requestDto);
    // 5. 반환
    return StatusDto.builder()
        .status(HttpStatus.CREATED.value())
        .message("예약 완료")
        .build();
  }

  public void saveReservations(User user, Cafe cafe, ReservationCreateRequestDto requestDto) {
    Reservation reservation = requestDto.convertDtoToEntity(cafe, user);
    List<ReservationDetail> reservationDetails =
        requestDto.convertEntityToDtoByReservationDetail(reservation);
    reservationCalculationService.calcReservation(reservation, reservationDetails);
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
            .status(String.valueOf(reservation.getStatus()))
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
            .status(String.valueOf(reservation.getStatus()))
            .totalPrice(reservation.getTotalPrice())
            .build()
    );
    return PageResponseDto.success(responseDto, HttpStatus.OK, "예약 내역 조회(카페용) 성공");
  }

  // 예약 승인
  @Transactional
  public StatusDto approveReservation(AuthUser authUser, Long reservationId) {
    if (!authUser.getRoleType().equals(RoleType.OWNER)) {
      throw new BusinessException(ErrorCode.FORBIDDEN);
    }
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));
    if (reservation.getStatus() != ReservationStatus.PENDING) {
      throw new BusinessException(ErrorCode.INVALID_STATUS_CHANGE);
    }
    reservation.approve();
    reservationRepository.save(reservation);

    return StatusDto.builder()
        .status(HttpStatus.CREATED.value())
        .message("예약 승인")
        .build();
  }

  // 예약 상태 변경: 결제 완료 상황 (결제 여부 확인 메서드 사용) -> Complete)
  @Transactional
  public StatusDto confirmPayment(AuthUser authUser, Long reservationId) {
    if (!authUser.getRoleType().equals(RoleType.USER)) {
      throw new BusinessException(ErrorCode.FORBIDDEN);
    }
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

    Reservation updatedReservation = reservation.confirmPayment();
    reservationRepository.save(updatedReservation);

    return StatusDto.builder()
        .status(HttpStatus.CREATED.value())
        .message("결제 확인 -> 예약 완료")
        .build();
  }

  // 예약 취소: 사용자용
  @Transactional
  public StatusDto cancelReservationByUser(AuthUser authUser, Long reservationId) {
    Long userId = authUser.getId();
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));
    if (!authUser.getRoleType().equals(RoleType.USER)) {
      throw new BusinessException(ErrorCode.FORBIDDEN);
    }
    if (!reservation.getUser().getId().equals(userId)) {
      throw new BusinessException(ErrorCode.UNAUTHORIZED);
    }
    reservation.cancelByUser();
    ;
    reservationRepository.save(reservation);

    return StatusDto.builder()
        .status(HttpStatus.CREATED.value())
        .message("사용자가 예약을 취소했습니다.")
        .build();
  }

  @Transactional
  public StatusDto cancelReservationByOwner(AuthUser authUser, Long reservationId, Long cafeId) {
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

    Cafe cafe = reservation.getCafe();
    // 요청한 카페와 예약한 카페가 일치하는지
    if (!cafe.getId().equals(cafeId)) {
      throw new BusinessException(ErrorCode.UNAUTHORIZED);
    }
    // 관리자가 해당 카페 소유자인지
    if (!cafe.getUser().getId().equals(authUser.getId())) {
      throw new BusinessException(ErrorCode.UNAUTHORIZED);
    }
    // 에약 상태 검증(Complete 상태는 취소 불가)
    if (reservation.getStatus() == ReservationStatus.COMPLETED) {
      throw new BusinessException(ErrorCode.INVALID_STATUS_CHANGE);
    }
    reservation.cancelByOwner();
    reservationRepository.save(reservation);

    return StatusDto.builder()
        .status(HttpStatus.CREATED.value())
        .message("사장님이 예약을 취소했습니다.")
        .build();
  }


}
