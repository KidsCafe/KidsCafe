package com.sparta.kidscafe.domain.reservation.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.kidscafe.common.entity.condition.CafeCondition;
import com.sparta.kidscafe.common.entity.condition.ReservationCondition;
import com.sparta.kidscafe.common.entity.condition.RoomCondition;
import com.sparta.kidscafe.common.enums.TargetType;
import com.sparta.kidscafe.domain.cafe.entity.QCafe;
import com.sparta.kidscafe.domain.reservation.entity.QReservation;
import com.sparta.kidscafe.domain.reservation.entity.QReservationDetail;
import com.sparta.kidscafe.domain.reservation.repository.condition.ReservationSearchCondition;
import com.sparta.kidscafe.domain.room.entity.QRoom;
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

  @Override
  public Boolean isRoomAvailable(ReservationSearchCondition condition) {
    return queryFactory
        .select(reservationDetail
            .count
            .sum()
            .add(condition.getUserCount())
            .loe(roomMaxCountQuery(condition)))
        .from(reservationDetail)
        .leftJoin(reservation).on(reservationDetail.reservation.id.eq(reservation.id))
        .leftJoin(cafe).on(cafe.id.eq(reservation.cafe.id))
        .where(isRoomAvailableMakeWhere(condition))
        .fetchOne();
  }

  private JPQLQuery<Integer> roomMaxCountQuery(ReservationSearchCondition condition) {
    return queryFactory
        .select(room.maxCount)
        .from(room)
        .where(roomCondition.eqId(condition.getRoomId()));
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
        .and(reservationDetail.reservation.id.in(reservationRoomIdQuery(condition)))
        .and(reservationCondition.eqTargetType(TargetType.FEE))
        .and(cafeCondition.loeOpenedAt(condition.getStartedAt().toLocalTime()))
        .and(cafeCondition.goeClosedAt(condition.getFinishedAt().toLocalTime()))
        .and(cafeCondition.notHoliday(condition.getStartedAt()));
  }
}
