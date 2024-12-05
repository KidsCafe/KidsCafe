package com.sparta.kidscafe.domain.pricepolicy.service;

import com.sparta.kidscafe.domain.pricepolicy.repository.PricePolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PricePolicyService {
  private PricePolicyRepository repository;
}
