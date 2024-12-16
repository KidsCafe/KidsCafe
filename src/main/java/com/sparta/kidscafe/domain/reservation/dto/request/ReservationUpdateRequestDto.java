package com.sparta.kidscafe.domain.reservation.dto.request;

import com.sparta.kidscafe.common.enums.TargetType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationUpdateRequestDto {

  @NotNull
  private String startedAt;

  @NotNull
  private String finishedAt;

  @Valid
  private List<ReservationDetailUpdateRequestDto> details;

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ReservationDetailUpdateRequestDto {

    @NotNull
    private TargetType targetType;
    @NotNull
    private Long targetId;
    @NotNull
    private Long count;
  }
}

