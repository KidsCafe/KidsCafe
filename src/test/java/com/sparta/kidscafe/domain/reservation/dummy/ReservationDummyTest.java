package com.sparta.kidscafe.domain.reservation.dummy;

import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.fee.entity.Fee;
import com.sparta.kidscafe.domain.reservation.entity.Reservation;
import com.sparta.kidscafe.domain.reservation.entity.ReservationDetail;
import com.sparta.kidscafe.domain.reservation.repository.ReservationDetailRepository;
import com.sparta.kidscafe.domain.reservation.repository.ReservationRepository;
import com.sparta.kidscafe.domain.room.entity.Room;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import com.sparta.kidscafe.dummy.DummyReservation;
import com.sparta.kidscafe.dummy.DummyReservationDetail;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class ReservationDummyTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  CafeRepository cafeRepository;

  @Autowired
  ReservationRepository reservationRepository;

  @Autowired
  ReservationDetailRepository reservationDetailRepository;

  @Test
  @Transactional
  @Rollback(value = false)
  void createReservation() {
    // user dummy test 돌려야함
    // cafe dummy test 돌려야함
    List<User> users = userRepository.findAllByRole(RoleType.USER);
    List<Cafe> cafes = cafeRepository.findAll();

    for (User user : users) {
      for (Cafe cafe : cafes) {
        List<Fee> fees = cafe.getFees();
        List<Room> rooms = cafe.getRooms();

        Reservation reservation = DummyReservation.createDummyReservation(user, cafe);
        reservationRepository.save(reservation);

        List<ReservationDetail> reservationDetails =
            DummyReservationDetail.createDummyReservationDetailsByFee(reservation, fees);
        reservationDetails.addAll(
            DummyReservationDetail.createDummyReservationDetailsByRoom(reservation, rooms));
        reservationDetailRepository.saveAll(reservationDetails);
        DummyReservation.updateDummyReservationTotalPrice(reservation, reservationDetails);
      }
    }
  }
}
