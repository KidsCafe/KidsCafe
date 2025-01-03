package com.sparta.kidscafe.domain.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentResponseDto {
  private String paymentUrl; // 결제 링크 (카카오페이 송금 링크)
  private String status; // 결제 상태 (예: PENDING, SUCCESS, FAILED)

  public static PaymentResponseDto createPendingResponse(String paymentUrl) {
    return new PaymentResponseDto(paymentUrl, "PENDING");
  }
}
