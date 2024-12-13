package com.sparta.kidscafe.domain.reservation.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.kidscafe.domain.cafe.entity.QCafe;
import com.sparta.kidscafe.domain.reservation.repository.condition.ReservationSearchCondition;
import com.sparta.kidscafe.domain.room.entity.QRoom;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReservationDslRepositoryImpl implements ReservationDslRepository {
  private final JPAQueryFactory queryFactory;
  private  QRoom room = QRoom.room;
  private QCafe cafe = QCafe.cafe;

  @Override
  public boolean isRoomAvailable(ReservationSearchCondition condition) {

    return false;
  }
}
