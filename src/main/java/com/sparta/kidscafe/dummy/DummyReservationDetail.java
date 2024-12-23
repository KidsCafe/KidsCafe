package com.sparta.kidscafe.dummy;

import com.sparta.kidscafe.common.enums.TargetType;
import com.sparta.kidscafe.common.util.TestUtil;
import com.sparta.kidscafe.domain.fee.entity.Fee;
import com.sparta.kidscafe.domain.reservation.entity.Reservation;
import com.sparta.kidscafe.domain.reservation.entity.ReservationDetail;
import com.sparta.kidscafe.domain.room.entity.Room;

import java.util.ArrayList;
import java.util.List;

public class DummyReservationDetail {
  public static ReservationDetail createDummyReservationDetail(Reservation reservation, Fee fee) {
    Long randomCount = Long.parseLong(TestUtil.getRandomInteger(1, 5) + "");
    return ReservationDetail.builder()
        .reservation(reservation)
        .targetType(TargetType.FEE)
        .targetId(fee.getId())
        .price(fee.getFee())
        .count(randomCount * fee.getFee())
        .build();
  }

  public static ReservationDetail createDummyReservationDetail(Reservation reservation, Room room) {
    return ReservationDetail.builder()
        .reservation(reservation)
        .targetType(TargetType.ROOM)
        .targetId(room.getId())
        .price(room.getPrice())
        .count(1L)
        .build();
  }

  public static List<ReservationDetail> createDummyReservationDetailsByRoom(Reservation reservations, List<Room> rooms) {
    List<ReservationDetail> reservationDetails = new ArrayList<>();
    for (Room room : rooms)
      reservationDetails.add(createDummyReservationDetail(reservations, room));
    return reservationDetails;
  }

  public static List<ReservationDetail> createDummyReservationDetailsByFee(Reservation reservations, List<Fee> fees) {
    List<ReservationDetail> reservationDetails = new ArrayList<>();
    for (Fee fee : fees)
      reservationDetails.add(createDummyReservationDetail(reservations, fee));
    return reservationDetails;
  }
}
