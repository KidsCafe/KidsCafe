package com.sparta.kidscafe.domain.reservation.service;

import com.sparta.kidscafe.common.enums.TargetType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.pricepolicy.dto.response.PricePolicyDto;
import com.sparta.kidscafe.domain.pricepolicy.repository.PricePolicyRepository;
import com.sparta.kidscafe.domain.pricepolicy.searchcondition.PricePolicySearchCondition;
import com.sparta.kidscafe.domain.reservation.entity.Reservation;
import com.sparta.kidscafe.domain.reservation.entity.ReservationDetail;
import com.sparta.kidscafe.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationCalculationService {

  private final PricePolicyRepository pricePolicyRepository;

  public void calcReservation(
      Cafe cafe,
      User user,
      Reservation reservation,
      List<ReservationDetail> reservationDetails) {
    double totalPrice = 0;
    for (ReservationDetail reservationdetail : reservationDetails) {
      double price;
      PricePolicyDto dto;
      PricePolicySearchCondition condition =
          PricePolicySearchCondition.create(reservation, reservationdetail);

      if (reservationdetail.getTargetType().equals(TargetType.FEE)) {
        dto = pricePolicyRepository.findPricePolicyWithFee(condition);
      } else {
        dto = pricePolicyRepository.findPricePolicyWithRoom(condition);
      }

      price = (reservationdetail.getCount() * dto.getPrice()) * dto.getRate();
      reservationdetail.updatePrice((int) price);
      reservationDetails.add(reservationdetail);
      totalPrice += price;
    }

    reservation.updateTotalPrice((int) totalPrice);
  }
}
