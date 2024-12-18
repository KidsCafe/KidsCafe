package com.sparta.kidscafe.domain.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

  @Value("${payment.toss.api-url}")
  private String apiUrl;

  @Value("${payment.toss.secret_key}")
  private String secretKey;


}
