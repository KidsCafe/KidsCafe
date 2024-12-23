package com.sparta.kidscafe.domain.reservation.dto.request;

import com.sparta.kidscafe.common.enums.TargetType;
import com.sparta.kidscafe.domain.reservation.repository.condition.ReservationSearchCondition;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationUpdateRequestDto {

  private Long id;

  @NotNull
  private String startedAt;

  @NotNull
  private String finishedAt;

  @Valid
  private List<ReservationDetailUpdateRequestDto> details;

  public ReservationSearchCondition createSearchCondition(Long cafeId, Long reservationId) {
    return ReservationSearchCondition.builder()
        .reservationId(reservationId)
        .cafeId(cafeId)
        .roomId(getRoomId())
        .userCount(getCount())
        .startedAt(LocalDateTime.parse(startedAt))
        .finishedAt(LocalDateTime.parse(finishedAt))
        .build();
  }

  public Long getRoomId() {
    for (ReservationDetailUpdateRequestDto detail : details) {
      if (detail.getTargetType().equals(TargetType.ROOM)) {
        return detail.getTargetId();
      }
    }
    return null;
  }

  public int getCount() {
    int totalCount = 0;
    for (ReservationDetailUpdateRequestDto detail : details) {
      totalCount += detail.getCount();
    }
    return totalCount;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ReservationDetailUpdateRequestDto {

    private Long id;
    @NotNull
    private TargetType targetType;
    @NotNull
    private Long targetId;
    @NotNull
    private Long count;
  }
}

