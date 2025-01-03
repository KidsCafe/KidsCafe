package com.sparta.kidscafe.domain.payment.controller;

import com.sparta.kidscafe.domain.payment.dto.request.PaymentRequestDto;
import com.sparta.kidscafe.domain.payment.dto.response.PaymentResponseDto;
import com.sparta.kidscafe.domain.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

  private final PaymentService paymentService;

  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @PostMapping
  public ResponseEntity<PaymentResponseDto> createPayment(@RequestBody PaymentRequestDto requestDto) {
    PaymentResponseDto response = paymentService.createPayment(requestDto);
    return ResponseEntity.ok(response);
  }
}
