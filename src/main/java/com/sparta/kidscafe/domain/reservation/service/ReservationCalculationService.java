package com.sparta.kidscafe.domain.reservation.service;

import com.sparta.kidscafe.common.enums.TargetType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.fee.entity.Fee;
import com.sparta.kidscafe.domain.fee.repository.FeeRepository;
import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import com.sparta.kidscafe.domain.pricepolicy.repository.PricePolicyRepository;
import com.sparta.kidscafe.domain.reservation.dto.request.ReservationCreateRequestDto;
import com.sparta.kidscafe.domain.room.entity.Room;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationCalculationService {

  private final PricePolicyRepository pricePolicyRepository;
  private final FeeRepository feeRepository;

  public int calculateTotalPrice(Cafe cafe, Room room,
      List<ReservationCreateRequestDto.ReservationDetailRequestDto> details) {
    // 기본 룸 가격 추가
    int totalPrice = room.getPrice();

    for (ReservationCreateRequestDto.ReservationDetailRequestDto detail : details) {
      if (detail.getTargetType() == TargetType.FEE) {
        Fee fee = feeRepository.findById(detail.getTargetId())
            .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_FEE_ID));
        totalPrice += fee.getFee() * detail.getCount();
      } else {
        throw new BusinessException(ErrorCode.UNSUPPORTED_TARGET_TYPE);
      }
    }
    // PricePolicy에 따른 배율 적용
    List<PricePolicy> policies = pricePolicyRepository.findByCafeIdAndDayTypeContains(cafe.getId(),
        getCurrentDayType());
    for (PricePolicy policy : policies) {
      totalPrice *= policy.getRate();
    }
    return totalPrice;
  }

  private String getCurrentDayType() {
    String today = LocalDate
        .now()
        .getDayOfWeek()
        .getDisplayName(TextStyle.SHORT, Locale.KOREAN);
    return today;
  }
}
