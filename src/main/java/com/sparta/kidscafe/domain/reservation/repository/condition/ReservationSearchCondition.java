package com.sparta.kidscafe.domain.reservation.repository.condition;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservationSearchCondition {
  private Long cafeId;
  private Long roomId;
  private Integer userCount;
  private LocalDateTime startedAt;
  private LocalDateTime finishedAt;
}
