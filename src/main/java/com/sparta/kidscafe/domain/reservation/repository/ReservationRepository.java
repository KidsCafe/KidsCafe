package com.sparta.kidscafe.domain.reservation.repository;

import com.sparta.kidscafe.domain.reservation.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  Page<Reservation> findByUserId(Long userId, Pageable pageable);

  Page<Reservation> findByCafeId(Long cafeId, Pageable pageable);
}
