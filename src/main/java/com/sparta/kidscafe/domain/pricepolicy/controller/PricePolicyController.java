package com.sparta.kidscafe.domain.pricepolicy.controller;

import com.sparta.kidscafe.domain.pricepolicy.service.PricePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PricePolicyController {
  private final PricePolicyService pricePolicyService;
}
