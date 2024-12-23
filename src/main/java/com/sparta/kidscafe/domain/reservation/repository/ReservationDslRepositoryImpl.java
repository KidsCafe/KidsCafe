package com.sparta.kidscafe.domain.reservation.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.kidscafe.common.enums.TargetType;
import com.sparta.kidscafe.domain.cafe.entity.QCafe;
import com.sparta.kidscafe.domain.cafe.repository.condition.CafeCondition;
import com.sparta.kidscafe.domain.reservation.entity.QReservation;
import com.sparta.kidscafe.domain.reservation.entity.QReservationDetail;
import com.sparta.kidscafe.domain.reservation.repository.condition.ReservationCondition;
import com.sparta.kidscafe.domain.reservation.repository.condition.ReservationSearchCondition;
import com.sparta.kidscafe.domain.room.entity.QRoom;
import com.sparta.kidscafe.domain.room.repository.condition.RoomCondition;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReservationDslRepositoryImpl implements ReservationDslRepository {

  private final JPAQueryFactory queryFactory;
  private final CafeCondition cafeCondition;
  private final RoomCondition roomCondition;
  private final ReservationCondition reservationCondition;

  private final QRoom room = QRoom.room;
  private final QCafe cafe = QCafe.cafe;
  private final QReservation reservation = QReservation.reservation;
  private final QReservationDetail reservationDetail = QReservationDetail.reservationDetail;

//  @Override
//  public boolean isRoomAvailable(ReservationSearchCondition condition) {
//    Boolean isRoomAvailable = queryFactory
//        .select(reservationDetail
//            .count
//            .sum()
//            .add(condition.getUserCount())
//            .loe(roomMaxCountQuery(condition)))
//        .from(reservationDetail)
//        .leftJoin(reservation).on(reservationDetail.reservation.id.eq(reservation.id))
//        .leftJoin(cafe).on(cafe.id.eq(reservation.cafe.id))
//        .where(isRoomAvailableMakeWhere(condition))
//        .fetchOne();
//
//    return isRoomAvailable == null || isRoomAvailable;
//  }

  @Override
  public boolean isRoomAvailable(ReservationSearchCondition condition) {
    Boolean isRoomAvailable = queryFactory
        .select(reservation.id.count().loe(1))
        .from(reservationDetail)
        .leftJoin(reservation).on(reservationDetail.reservation.id.eq(reservation.id))
        .leftJoin(cafe).on(cafe.id.eq(reservation.cafe.id))
        .where(isRoomAvailableMakeWhere(condition))
        .fetchOne();

    return isRoomAvailable == null || isRoomAvailable;
  }

  private JPQLQuery<Integer> roomMaxCountQuery(ReservationSearchCondition condition) {
    return queryFactory
        .select(room.maxCount)
        .from(room)
        .where(roomCondition.eq(room.id, condition.getRoomId()));
  }

  private JPQLQuery<Long> reservationRoomIdQuery(ReservationSearchCondition condition) {
    return queryFactory
        .select(reservation.id)
        .from(reservation)
        .leftJoin(reservationDetail)
        .on(reservationDetail.reservation.id.eq(reservation.id))
        .where(reservationRoomIdMakeWhere(condition));
  }

  private BooleanBuilder reservationRoomIdMakeWhere(ReservationSearchCondition condition) {
    return new BooleanBuilder()
        .and(reservationCondition.eqTargetType(TargetType.ROOM))
        .and(reservationCondition.eqTargetId(condition.getRoomId()))
        .and(reservationCondition.betweenReservationAt(condition));
  }

  private BooleanBuilder isRoomAvailableMakeWhere(ReservationSearchCondition condition) {
    return new BooleanBuilder()
        .and(reservationCondition.notEqId(condition.getReservationId()))
        .and(reservationCondition.inReservationDetailId(reservationRoomIdQuery(condition)))
        .and(reservationCondition.eqTargetType(TargetType.FEE))
        .and(cafeCondition.isOpening(condition.getStartedAt()));
  }
}
