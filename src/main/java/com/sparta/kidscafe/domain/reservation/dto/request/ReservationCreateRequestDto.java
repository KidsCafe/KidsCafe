package com.sparta.kidscafe.domain.reservation.dto.request;

import com.sparta.kidscafe.common.enums.TargetType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCreateRequestDto {

  private LocalDateTime startedAt;
  private LocalDateTime finishedAt;
  private List<ReservationDetailRequestDto> details;

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ReservationDetailRequestDto {

    private TargetType targetType;
    private Long targetId;
    private int count;
  }
}
