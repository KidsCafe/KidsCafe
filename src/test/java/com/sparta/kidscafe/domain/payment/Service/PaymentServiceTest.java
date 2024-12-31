package com.sparta.kidscafe.domain.payment.Service;

import com.sparta.kidscafe.domain.payment.dto.request.PaymentRequestDto;
import com.sparta.kidscafe.domain.payment.dto.response.PaymentResponseDto;
import com.sparta.kidscafe.domain.payment.service.PaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentServiceTest {

  private final PaymentService paymentService = new PaymentService();

  @Test
  @DisplayName("결제 생성 성공 테스트")
  void createPayment_Success() {
    // Given
    PaymentRequestDto requestDto = new PaymentRequestDto(123L, 20000, "키즈카페 예약", "http://localhost:8080/success");

    // When
    PaymentResponseDto responseDto = paymentService.createPayment(requestDto);

    // Then
    assertThat(responseDto).isNotNull();
    assertThat(responseDto.getPaymentUrl()).isEqualTo("https://qr.kakaopay.com/Ej8j7TXTL");
    assertThat(responseDto.getStatus()).isEqualTo("PENDING");
  }

  @Test
  @DisplayName("결제 생성 실패 테스트 - 금액 누락")
  void createPayment_Failure_MissingAmount() {
    // Given
    PaymentRequestDto requestDto = new PaymentRequestDto();
    requestDto.setReservationId(123L);
    requestDto.setOrderName("키즈카페 예약");
    requestDto.setCallbackUrl("http://localhost:8080/success");

    // When & Then
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      paymentService.createPayment(requestDto);
    });
    assertThat(exception.getMessage()).isEqualTo("결제 금액은 필수이며 0보다 커야 합니다."); // 메시지 수정
  }

  @Test
  @DisplayName("결제 생성 실패 테스트 - 잘못된 URL")
  void createPayment_Failure_InvalidUrl() {
    // Given
    PaymentRequestDto requestDto = new PaymentRequestDto(123L, 20000, "키즈카페 예약", "invalid-url");

    // When & Then
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      paymentService.createPayment(requestDto);
    });
    assertThat(exception.getMessage()).isEqualTo("올바른 URL 형식이 아닙니다.");
  }
}

