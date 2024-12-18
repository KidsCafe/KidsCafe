package com.sparta.kidscafe.domain.pricepolicy.searchcondition;

import com.sparta.kidscafe.common.enums.TargetType;
import com.sparta.kidscafe.domain.reservation.entity.Reservation;
import com.sparta.kidscafe.domain.reservation.entity.ReservationDetail;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

@Builder
@Getter
public class PricePolicySearchCondition {

    private Long cafeId;
    private TargetType targetType;
    private Long targetId;
    private String working;

    public static PricePolicySearchCondition create(
            Reservation reservation,
            ReservationDetail detail) {
        // 예약 날짜
        LocalDateTime startedAt = reservation.getStartedAt();
        String today = startedAt
                .getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, Locale.KOREAN);

        return PricePolicySearchCondition.builder()
                .targetType(detail.getTargetType())
                .targetId(detail.getTargetId())
                .working(today)
                .build();
    }
}
