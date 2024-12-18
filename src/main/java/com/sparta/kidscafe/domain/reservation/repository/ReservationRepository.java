package com.sparta.kidscafe.domain.reservation.repository;

import com.sparta.kidscafe.domain.reservation.entity.Reservation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationDslRepository {

  Page<Reservation> findByUserId(Long userId, Pageable pageable);

  Page<Reservation> findByCafeId(Long cafeId, Pageable pageable);

  Optional<Reservation> findByUserIdAndId(Long userId, Long reservationId);

}
