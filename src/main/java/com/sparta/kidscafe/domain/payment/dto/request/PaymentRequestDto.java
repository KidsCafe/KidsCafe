package com.sparta.kidscafe.domain.payment.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentRequestDto {
  private Long reservationId; // 예약 ID
  private int amount; // 결제 금액
  private String orderName; // 주문 이름 (예: 키즈카페 예약)
  private String callbackUrl; // 결제 완료 후 리다이렉트할 URL

  public PaymentRequestDto(Long reservationId, int amount, String orderName, String callbackUrl) {
    this.reservationId = reservationId;
    this.amount = amount;
    this.orderName = orderName;
    this.callbackUrl = callbackUrl;
  }
}
