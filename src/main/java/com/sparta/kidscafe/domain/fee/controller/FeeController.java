package com.sparta.kidscafe.domain.fee.controller;

import com.sparta.kidscafe.domain.fee.service.FeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FeeController {
  private final FeeService feeService;
}
