package com.sparta.kidscafe.dummy;

import com.sparta.kidscafe.common.util.TestUtil;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.reservation.entity.Reservation;
import com.sparta.kidscafe.domain.reservation.entity.ReservationDetail;
import com.sparta.kidscafe.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;

public class DummyReservation {

  public static Reservation createDummyReservation(User user, Cafe cafe) {
    LocalDateTime randomStartedAt = TestUtil.getRandomLocalDateTime(
        10,
        14
    );

    LocalDateTime randomFinishedAt = TestUtil.getRandomLocalDateTime(
        randomStartedAt.plusHours(1).getHour(),
        21
    );

    LocalDateTime tempDateTime = randomStartedAt;
    if(randomFinishedAt.isBefore(randomStartedAt)) {
      randomStartedAt = randomFinishedAt;
      randomFinishedAt = tempDateTime;
    }

    return Reservation.builder()
        .cafe(cafe)
        .user(user)
        .startedAt(randomStartedAt)
        .finishedAt(randomFinishedAt)
        .totalPrice(0)
        .build();
  }

  public static void updateDummyReservationTotalPrice(Reservation reservation, List<ReservationDetail> details) {
    int totalPrice = 0;
    for (ReservationDetail detail : details) {
      totalPrice += detail.getPrice();
    }
    reservation.updateTotalPrice(totalPrice);
  }
}
