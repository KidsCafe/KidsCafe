package com.sparta.kidscafe.domain.reservation.repository.condition;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.sparta.kidscafe.common.enums.TargetType;
import com.sparta.kidscafe.domain.reservation.entity.QReservation;
import com.sparta.kidscafe.domain.reservation.entity.QReservationDetail;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReservationCondition {

  private final QReservation reservation = QReservation.reservation;
  private final QReservationDetail reservationDetail = QReservationDetail.reservationDetail;

  public BooleanExpression notEqId(Long id) {
    if (id == null)
      return null;

    return reservation.id.notIn(id);
  }

  public BooleanBuilder eqTargetType(TargetType targetType) {
    return new BooleanBuilder()
        .and(reservationDetail.targetType.eq(targetType));
  }

  public BooleanBuilder eqTargetId(Long targetId) {
    if (targetId == null)
      return null;

    return new BooleanBuilder()
        .and(reservationDetail.targetId.eq(targetId));
  }

  public Predicate betweenReservationAt(ReservationSearchCondition condition) {
    LocalDateTime startedAt = condition.getStartedAt();
    LocalDateTime finishedAt = condition.getFinishedAt();

    if (startedAt == null || finishedAt == null) {
      return null;
    }

    BooleanBuilder innerBuilder = new BooleanBuilder();
    innerBuilder.and(reservation.finishedAt.goe(startedAt));
    innerBuilder.and(reservation.startedAt.loe(finishedAt));
    return innerBuilder.getValue();
  }

  public BooleanExpression inReservationDetailId(SubQueryExpression<Long> ids) {
    if (ids == null)
      return null;

    return reservationDetail.reservation.id.in(ids);
  }
}
