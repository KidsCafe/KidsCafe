package com.sparta.kidscafe.domain.payment.service;

import com.sparta.kidscafe.domain.payment.dto.request.PaymentRequestDto;
import com.sparta.kidscafe.domain.payment.dto.response.PaymentResponseDto;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

  private static final String KAKAO_PAY_URL = "https://qr.kakaopay.com/Ej8j7TXTL";

  public PaymentResponseDto createPayment(PaymentRequestDto requestDto) {
    validatePaymentRequest(requestDto);

    // 결제 URL 반환 (현재 고정된 URL 사용)
    return PaymentResponseDto.createPendingResponse(KAKAO_PAY_URL);
  }

  private void validatePaymentRequest(PaymentRequestDto requestDto) {
    if (requestDto.getAmount() <= 0) {
      throw new IllegalArgumentException("결제 금액은 필수이며 0보다 커야 합니다.");
    }
    if (!isValidUrl(requestDto.getCallbackUrl())) {
      throw new IllegalArgumentException("올바른 URL 형식이 아닙니다.");
    }
  }

  private boolean isValidUrl(String url) {
    try {
      new java.net.URL(url);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
