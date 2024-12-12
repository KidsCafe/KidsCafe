package com.sparta.kidscafe.domain.reservation.dto.request;

import com.sparta.kidscafe.common.enums.TargetType;
import com.sparta.kidscafe.domain.reservation.entity.Reservation;
import com.sparta.kidscafe.domain.reservation.entity.ReservationDetail;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public ReservationDetail convertDtoToEntity() {
      // Todo. JS 값 세팅 필요
      ReservationDetail reservationDetail = new ReservationDetail();
      return reservationDetail;
    }
  }

  public Reservation convertDtoToEntity(){
    // Todo. JS 값 세팅 필요
    Reservation reservation = new Reservation();
    return reservation;
  }

  public List<ReservationDetail> convertEntityToDtoByReservationDetail(){
    List<ReservationDetail> reservationDetails = new ArrayList<>();
    for(ReservationDetailRequestDto detail : details){
      reservationDetails.add(detail.convertDtoToEntity());
    }
    return reservationDetails;
  }
}
