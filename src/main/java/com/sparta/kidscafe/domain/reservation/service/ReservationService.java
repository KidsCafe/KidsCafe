package com.sparta.kidscafe.domain.reservation.service;

import com.sparta.kidscafe.domain.reservation.repository.ReservationDetailRepository;
import com.sparta.kidscafe.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {
  private final ReservationRepository reservationRepository;
  private final ReservationDetailRepository reservationDetailRepository;
}
