package com.sparta.kidscafe.domain.reservation.dto.response;

import com.sparta.kidscafe.common.enums.TargetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDto {

  private Long reservationId;
  private Long userId;
  private Long cafeId;
  private String userName;
  private String cafeName;
  private LocalDateTime startedAt;
  private LocalDateTime finishedAt;
  private int totalPrice;
  private String status;
  private List<ReservationDetailResponseDto> details;

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ReservationDetailResponseDto {
    private TargetType targetType;
    private Long targetId;
    private String targetName;
    private int price;
    private Long count;
  }
}
