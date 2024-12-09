package com.sparta.kidscafe.domain.pricepolicy.service;


import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.pricepolicy.dto.request.PricePolicyCreateRequestDto;
import com.sparta.kidscafe.domain.pricepolicy.dto.response.PricePolicyResponseDto;
import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import com.sparta.kidscafe.domain.pricepolicy.repository.PricePolicyRepository;
import com.sparta.kidscafe.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PricePolicyService {

  private final CafeRepository cafeRepository;
  private final PricePolicyRepository pricePolicyRepository;

  @Transactional
  public void addPricePolicy(Long cafeId, PricePolicyCreateRequestDto requestDto) {
    // 1. Cafe 조회
    Cafe cafe = cafeRepository.findById(cafeId)
            .orElseThrow(() -> new IllegalArgumentException(ErrorCode.CAFE_NOT_FOUND.getMessage()));

    // 2. PricePolicy 엔티티 생성
    PricePolicy pricePolicy = PricePolicy.builder()
            .cafe(cafe)
            .targetType(requestDto.getTargetType())
            .targetId(requestDto.getTargetId())
            .title(requestDto.getTitle())
            .dayType(requestDto.getDayType())
            .rate(requestDto.getRate())
            .build();

    // 3. 저장
    pricePolicyRepository.save(pricePolicy);
  }

  @Transactional(readOnly = true)
  public List<PricePolicyResponseDto> getPricePolicies(Long cafeId) {
    List<PricePolicy> pricePolicies = pricePolicyRepository.findAllByCafeId(cafeId);

    return pricePolicies.stream()
            .map(policy -> PricePolicyResponseDto.fromEntity(policy, policy.getTargetName()))
            .collect(Collectors.toList());
  }
}
