package com.sparta.kidscafe.domain.reservation.repository.condition;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReservationSearchCondition {
  private Long reservationId;
  private Long cafeId;
  private Long roomId;
  private Integer userCount;
  private LocalDateTime startedAt;
  private LocalDateTime finishedAt;
}
