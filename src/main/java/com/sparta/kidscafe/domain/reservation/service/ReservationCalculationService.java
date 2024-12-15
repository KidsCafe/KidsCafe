package com.sparta.kidscafe.domain.reservation.service;

import com.sparta.kidscafe.common.enums.TargetType;
import com.sparta.kidscafe.domain.fee.repository.FeeRepository;
import com.sparta.kidscafe.domain.pricepolicy.repository.PricePolicyRepository;
import com.sparta.kidscafe.domain.pricepolicy.searchcondition.PricePolicySearchCondition;
import com.sparta.kidscafe.domain.reservation.entity.Reservation;
import com.sparta.kidscafe.domain.reservation.entity.ReservationDetail;
import com.sparta.kidscafe.domain.room.repository.RoomRepository;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationCalculationService {

  private final PricePolicyRepository pricePolicyRepository;
  private final FeeRepository feeRepository;
  private final RoomRepository roomRepository;

  public void calcReservation(
      Reservation reservation,
      List<ReservationDetail> reservationDetails) {
    double totalPrice = 0;
    for (ReservationDetail reservationdetail : reservationDetails) {
      double price;
      PricePolicySearchCondition condition =
          PricePolicySearchCondition.create(reservation, reservationdetail);

      List<Double> rates;
      if (reservationdetail.getTargetType().equals(TargetType.FEE)) {
        price = feeRepository.findById(condition.getTargetId())
            .orElseThrow(() -> new BusinessException(ErrorCode.FEE_NOT_FOUND)).getFee();
        rates = pricePolicyRepository.findPricePolicyWithFee(condition);
      } else {
        price = roomRepository.findById(condition.getTargetId())
            .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND)).getPrice();
        rates = pricePolicyRepository.findPricePolicyWithRoom(condition);
      }

      price = reservationdetail.getPrice() * reservationdetail.getCount();
      for(Double rate : rates) {
        price *= rate;
      }

      reservationdetail.updatePrice((int) price);
      totalPrice += price;
    }

    reservation.updateTotalPrice((int) totalPrice);
  }
}
