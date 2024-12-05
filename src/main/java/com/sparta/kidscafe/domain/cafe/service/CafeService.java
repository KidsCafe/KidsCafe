package com.sparta.kidscafe.domain.cafe.service;

import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CafeService {
  private final CafeRepository cafeRepository;
}
