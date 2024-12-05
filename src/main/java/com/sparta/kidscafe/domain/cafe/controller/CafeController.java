package com.sparta.kidscafe.domain.cafe.controller;

import com.sparta.kidscafe.domain.cafe.service.CafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CafeController {
  private final CafeService cafeService;
}
