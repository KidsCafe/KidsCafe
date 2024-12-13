package com.sparta.kidscafe.common.entity.condition;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.sparta.kidscafe.domain.reservation.entity.QReservation;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class ReservationCondition {

  private final QReservation reservation = QReservation.reservation;

  public Predicate startedAtBetweenCafeOpening(LocalDateTime startedAt) {
    if(startedAt == null) {
      return null;
    }

    BooleanBuilder innerBuilder = new BooleanBuilder();
    innerBuilder.and(reservation.startedAt.goe(startedAt));
    innerBuilder.and(reservation.finishedAt.loe(startedAt));
    return innerBuilder.getValue();
  }

  public Predicate finishedAtBetweenCafeOpening(LocalDateTime finishedAt) {
    if(finishedAt == null) {
      return null;
    }

    BooleanBuilder innerBuilder = new BooleanBuilder();
    innerBuilder.and(reservation.startedAt.goe(finishedAt));
    innerBuilder.and(reservation.finishedAt.loe(finishedAt));
    return innerBuilder.getValue();
  }
}
