package com.sparta.kidscafe.domain.pricepolicy.service;


import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.pricepolicy.dto.request.PricePolicyCreateRequestDto;
import com.sparta.kidscafe.domain.pricepolicy.dto.request.PricePolicyUpdateRequestDto;
import com.sparta.kidscafe.domain.pricepolicy.dto.response.PricePolicyResponseDto;
import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import com.sparta.kidscafe.domain.pricepolicy.repository.PricePolicyRepository;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
  public StatusDto addPricePolicy(Long cafeId, PricePolicyCreateRequestDto requestDto) {
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
    return StatusDto.builder()
        .status(HttpStatus.CREATED.value())
        .message("가격 정책 추가")
        .build();
  }

  @Transactional(readOnly = true)
  public ListResponseDto<PricePolicyResponseDto> getPricePolicies(Long cafeId) {
    // Cafe 존재 여부 확인
    if (!cafeRepository.existsById(cafeId)) {
      throw new BusinessException(ErrorCode.CAFE_NOT_FOUND);
    }

    // 정책 조회 및 변환
    List<PricePolicyResponseDto> collect = pricePolicyRepository.findAllByCafeId(cafeId)
        .stream()
        .map(PricePolicyResponseDto::from)
        .collect(Collectors.toList());
    return ListResponseDto.success(
        collect,
        HttpStatus.OK,
        "카페 조회 성공"
    );
  }

  @Transactional
  public StatusDto updatePricePolicy(Long cafeId, Long pricePolicyId, PricePolicyUpdateRequestDto requestDto) {
    // 1. Cafe 조회
    Cafe cafe = cafeRepository.findById(cafeId)
        .orElseThrow(() -> new BusinessException(ErrorCode.CAFE_NOT_FOUND));

    // 2. PricePolicy 조회
    PricePolicy pricePolicy = pricePolicyRepository.findById(pricePolicyId)
        .orElseThrow(() -> new BusinessException(ErrorCode.PRICE_POLICY_NOT_FOUND));

    // 3. 동일 Cafe 소속인지 검증
    if (!pricePolicy.getCafe().equals(cafe)) {
      throw new BusinessException(ErrorCode.PRICE_POLICY_MISMATCH);
    }

    // 4. 업데이트
    pricePolicy.updateDetails(requestDto.getTargetId(), requestDto.getTitle(), requestDto.getDayType(), requestDto.getRate());
    return StatusDto.builder()
        .status(HttpStatus.OK.value())
        .message("가격 정책 수정 성공")
        .build();
  }

  @Transactional
  public void deletePricePolicy(Long cafeId, Long pricePolicyId) {
    // 1. Cafe 조회
    Cafe cafe = cafeRepository.findById(cafeId)
        .orElseThrow(() -> new BusinessException(ErrorCode.CAFE_NOT_FOUND));

    // 2. PricePolicy 조회
    PricePolicy pricePolicy = pricePolicyRepository.findById(pricePolicyId)
        .orElseThrow(() -> new BusinessException(ErrorCode.PRICE_POLICY_NOT_FOUND));

    // 3. 동일 Cafe 소속인지 검증
    if (!pricePolicy.getCafe().equals(cafe)) {
      throw new BusinessException(ErrorCode.PRICE_POLICY_MISMATCH);
    }

    // 4. 삭제
    pricePolicyRepository.delete(pricePolicy);
  }
}
