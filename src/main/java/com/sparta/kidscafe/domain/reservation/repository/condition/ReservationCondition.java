package com.sparta.kidscafe.domain.reservation.repository.condition;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.sparta.kidscafe.common.enums.TargetType;
import com.sparta.kidscafe.domain.reservation.entity.QReservation;
import com.sparta.kidscafe.domain.reservation.entity.QReservationDetail;
import com.sparta.kidscafe.domain.reservation.repository.condition.ReservationSearchCondition;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class ReservationCondition {

  private final QReservation reservation = QReservation.reservation;
  private final QReservationDetail reservationDetail = QReservationDetail.reservationDetail;

  public BooleanBuilder eqTargetType(TargetType targetType) {
    return new BooleanBuilder()
        .and(reservationDetail.targetType.eq(targetType));
  }

  public BooleanBuilder eqTargetId(Long targetId) {
    if(targetId == null)
      return null;

    return new BooleanBuilder()
        .and(reservationDetail.targetId.eq(targetId));
  }

  public Predicate betweenReservationAt(ReservationSearchCondition condition) {
    LocalDateTime startedAt = condition.getStartedAt();
    LocalDateTime finishedAt = condition.getFinishedAt();

    if(startedAt == null || finishedAt == null) {
      return null;
    }

    BooleanBuilder innerBuilder = new BooleanBuilder();
    innerBuilder.and(reservation.finishedAt.goe(startedAt));
    innerBuilder.and(reservation.startedAt.loe(finishedAt));
    return innerBuilder.getValue();
  }
}
