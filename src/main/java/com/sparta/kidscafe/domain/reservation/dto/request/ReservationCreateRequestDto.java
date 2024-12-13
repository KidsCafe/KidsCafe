package com.sparta.kidscafe.domain.reservation.dto.request;

import com.sparta.kidscafe.common.enums.TargetType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.reservation.entity.Reservation;
import com.sparta.kidscafe.domain.reservation.entity.ReservationDetail;
import com.sparta.kidscafe.domain.reservation.repository.condition.ReservationSearchCondition;
import com.sparta.kidscafe.domain.user.entity.User;
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
    private Long count;

    public ReservationDetail convertDtoToEntity(Reservation reservation) {
      return ReservationDetail.builder()
          .reservation(reservation)
          .targetType(targetType)
          .targetId(targetId)
          .count(count)
          .build();
    }
  }

  public Reservation convertDtoToEntity(Cafe cafe, User user) {
    return Reservation.builder()
        .user(user)
        .cafe(cafe)
        .startedAt(startedAt)
        .finishedAt(finishedAt)
        .build();
  }

  public List<ReservationDetail> convertEntityToDtoByReservationDetail(Reservation reservation) {
    List<ReservationDetail> reservationDetails = new ArrayList<>();
    for (ReservationDetailRequestDto detail : details) {
      reservationDetails.add(detail.convertDtoToEntity(reservation));
    }
    return reservationDetails;
  }
  public ReservationSearchCondition createSearchCondition(Long cafeId) {
    return ReservationSearchCondition.builder()
        .cafeId(cafeId)
        .userCount(getCount())
        .startedAt(startedAt)
        .finishedAt(finishedAt)
        .build();
  }

  public int getCount() {
    int totalCount = 0;
    for(ReservationDetailRequestDto detail : details) {
      totalCount += detail.getCount();
    }
    return totalCount;
  }
}
