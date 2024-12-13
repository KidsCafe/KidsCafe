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
import com.sparta.kidscafe.domain.reservation.enums.ReservationStatus;
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

  private final ReservationRepository reservationRepository;
  private final ReservationCalculationService reservationCalculationService;
  private final CafeRepository cafeRepository;
  private final UserRepository userRepository;
  private final ReservationDetailRepository reservationDetailRepository;

//  @Transactional
//  public StatusDto createReservation(AuthUser authUser, Long cafeId,
//      ReservationCreateRequestDto requestDto) {
//    // 1. 유저 확인
//    Long userId = authUser.getId();
//    User user = userRepository.findById(userId)
//        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
//    if (!authUser.getRoleType().equals(RoleType.USER)) {
//      throw new BusinessException(ErrorCode.BAD_REQUEST);
//    }
//    // 2. 카페 확인
//    Cafe cafe = cafeRepository.findById(cafeId)
//        .orElseThrow(() -> new BusinessException(ErrorCode.CAFE_NOT_FOUND));
//
//    // 3. 룸 확인
//    Room room = roomRepository.findById(requestDto.getDetails()
//            .stream().filter(item -> item.getTargetType().equals(TargetType.ROOM)).findFirst()
//            .get().getTargetId())
//        .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));
//    // 룸이 존재하지 않을 경우 예외 처리
//    if (!cafe.getRooms().contains(room)) {
//      throw new BusinessException(ErrorCode.BAD_REQUEST);
//    }
//
//    // 4. totalPrice 계산
//    int totalPrice = reservationCalculationService.calculateTotalPrice(cafe, room,
//        requestDto.getDetails());
//
//    // 5. 예약 생성
//    Reservation reservation = Reservation.builder()
//        .cafe(cafe)
//        .user(user)
//        .startedAt(requestDto.getStartedAt())
//        .finishedAt(requestDto.getFinishedAt())
//        .totalPrice(totalPrice)
//        .build();
//
//    reservationRepository.save(reservation);
//    return StatusDto.builder()
//        .status(HttpStatus.CREATED.value())
//        .message("예약 완료")
//        .build();
//  }
//
//  @Transactional
//  public StatusDto tempCreateReservation(AuthUser authUser, Long cafeId,
//      ReservationCreateRequestDto requestDto) {
//    // 1. 유저 확인
//    User user = userRepository.findById(authUser.getId())
//        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
//    if (!authUser.getRoleType().equals(RoleType.USER)) {
//      throw new BusinessException(ErrorCode.BAD_REQUEST);
//    }
//
//    // 2. 카페 확인
//    Cafe cafe = cafeRepository.findById(cafeId)
//        .orElseThrow(() -> new BusinessException(ErrorCode.CAFE_NOT_FOUND));
//
//    // 3. 예약 가능한 상태인지 조회
//    // TODO. JS dsl이나 JPQL로 어쩌구 저쩌구
//
//    // 4. 예약이 가능하다면 예약 생성
//    Reservation reservation = requestDto.convertDtoToEntity();
//    List<ReservationDetail> details = requestDto.convertEntityToDtoByReservationDetail();
//
//    // 5. 정책을 이용하여 일치하는것은 가격을 계산하여 업데이트
//    calcReservation(cafeId, reservation, details);
//
//    // 6. 저장한다.
//    reservationRepository.save(reservation);
//    reservationDetailRepository.saveAll(details);
//
//    // 7. 반환
//    return StatusDto.builder()
//        .status(HttpStatus.CREATED.value())
//        .message("예약 완료")
//        .build();
//  }
//
//  private void calcReservation(Long cafeId, Reservation reservation,
//      List<ReservationDetail> details) {
//    // TODO. JS 값 계산을 위한 오늘날짜에 적용되는 가격정책 불러오기
//    List<PricePolicy> pricePolies = pricePolicyRepository.findAllByCafeId(cafeId);
//
//    int totalPrice = 0;
//    for (PricePolicy pricePolicy : pricePolies) {
//      for (ReservationDetail detail : details) {
//        // 타겟이(ROOM, FEE)같다면
//        if (pricePolicy.getTargetType().equals(detail.getTargetType())) {
//          detail.updatePrice(pricePolicy.getRate()); // 가격 적용
//          totalPrice += detail.getPrice();
//        }
//      }
//    }
//    reservation.updateTotalPrice(totalPrice);
//  }

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

    //4. 예약
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
}
