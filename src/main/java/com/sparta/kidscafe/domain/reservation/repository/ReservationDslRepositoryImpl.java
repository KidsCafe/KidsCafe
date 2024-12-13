package com.sparta.kidscafe.domain.reservation.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.kidscafe.common.entity.condition.CafeCondition;
import com.sparta.kidscafe.common.entity.condition.ReservationCondition;
import com.sparta.kidscafe.common.entity.condition.RoomCondition;
import com.sparta.kidscafe.domain.cafe.entity.QCafe;
import com.sparta.kidscafe.domain.reservation.entity.QReservation;
import com.sparta.kidscafe.domain.reservation.entity.QReservationDetail;
import com.sparta.kidscafe.domain.reservation.repository.condition.ReservationSearchCondition;
import com.sparta.kidscafe.domain.room.entity.QRoom;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReservationDslRepositoryImpl implements ReservationDslRepository {

  private final JPAQueryFactory queryFactory;
  private final ReservationCondition reservationCondition;
  private final CafeCondition cafeCondition;
  private final RoomCondition roomCondition;

  private final QRoom room = QRoom.room;
  private final QCafe cafe = QCafe.cafe;
  private final QReservation reservation = QReservation.reservation;
  private final QReservationDetail reservationDetail = QReservationDetail.reservationDetail;

  @Override
  public boolean isRoomAvailable(ReservationSearchCondition condition) {
    Tuple subQuery = isRoomAvailableSubQuery(condition); // 예약하려는 시간대에 해당 방에 예약한 사용자의 수
    Tuple mainQuery = isRoomAvailableMainQuery(condition); // 가게가 오픈상태인지 점검

    Long reservedRoomId = subQuery.get(0, Long.class);
    long reservedCount = subQuery.get(1, Long.class) == null ? 0 : subQuery.get(1, Long.class);
    Long roomId = mainQuery.get(0, Long.class);
    long roomMaximum = mainQuery.get(1, Long.class) == null ? 0 : mainQuery.get(1, Long.class);

    if(Objects.equals(reservedRoomId, roomId)) {
      return reservedCount + condition.getUserCount() <= roomMaximum;
    }
    return false;
  }

  private Tuple isRoomAvailableSubQuery(ReservationSearchCondition condition) {
    return queryFactory
        .select(
            reservationDetail.targetId,
            reservationDetail.count.sum())
        .from(reservation)
        .leftJoin(reservationDetail)
        .on(reservationDetail.reservation.eq(reservation))
        .where(isRoomAvailableSubMakeWhere(condition))
        .groupBy(reservationDetail.targetId).fetchFirst();
  }

  private Tuple isRoomAvailableMainQuery(ReservationSearchCondition condition) {
    return queryFactory
        .select(
            room.id,
            room.maxCount)
        .from(room)
        .leftJoin(cafe).on(cafe.id.eq(room.id))
        .where(isRoomAvailableMainMakeWhere(condition))
        .fetchFirst();
  }

  private BooleanBuilder isRoomAvailableSubMakeWhere(ReservationSearchCondition condition) {
    return new BooleanBuilder()
        .and(roomCondition.eqId(condition.getRoomId()))
        .and(reservationCondition.startedAtBetweenCafeOpening(condition.getStartedAt()))
        .and(reservationCondition.finishedAtBetweenCafeOpening(condition.getFinishedAt()));
  }

  private BooleanBuilder isRoomAvailableMainMakeWhere(ReservationSearchCondition condition) {
    return new BooleanBuilder()
        .and(roomCondition.eqId(condition.getRoomId()))
        .and(cafeCondition.dayOff(condition.getStartedAt()));
  }
}
