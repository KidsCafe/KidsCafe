package com.sparta.kidscafe.domain.reservation.repository;

import com.sparta.kidscafe.domain.reservation.repository.condition.ReservationSearchCondition;

public interface ReservationDslRepository {

  boolean isRoomAvailable(ReservationSearchCondition condition);
}
