package com.sparta.kidscafe.domain.pricepolicy.service;

import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.enums.TargetType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.pricepolicy.dto.request.PricePolicyCreateRequestDto;
import com.sparta.kidscafe.domain.pricepolicy.dto.request.PricePolicyUpdateRequestDto;
import com.sparta.kidscafe.domain.pricepolicy.dto.response.PricePolicyResponseDto;
import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import com.sparta.kidscafe.domain.pricepolicy.repository.PricePolicyRepository;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Mockito 확장 사용
class PricePolicyServiceTest {

  @Mock
  private CafeRepository cafeRepository;

  @Mock
  private PricePolicyRepository pricePolicyRepository;

  @InjectMocks
  private PricePolicyService pricePolicyService;

  private Cafe cafe;
  private PricePolicy pricePolicy;

  @BeforeEach
  void setUp() {
    cafe = Cafe.builder().id(1L).name("Test Cafe").build();
    pricePolicy = PricePolicy.builder()
        .id(1L)
        .cafe(cafe)
        .title("Weekend Surcharge")
        .dayType("SAT, SUN")
        .rate(1.2)
        .build();
  }

  @Test
  @DisplayName("가격 정책 추가 성공")
  void addPricePolicy_Success() {
    PricePolicyCreateRequestDto requestDto = new PricePolicyCreateRequestDto(
        TargetType.FEE, 1L, "Weekend Surcharge", "SAT, SUN", 1.2);

    when(cafeRepository.findById(cafe.getId())).thenReturn(Optional.of(cafe));
    when(pricePolicyRepository.save(any(PricePolicy.class))).thenReturn(pricePolicy);

    StatusDto response = pricePolicyService.addPricePolicy(cafe.getId(), requestDto);

    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    assertThat(response.getMessage()).isEqualTo("가격 정책 추가");
  }

  @Test
  @DisplayName("가격 정책 추가 실패 - 카페 존재하지 않음")
  void addPricePolicy_Fail_CafeNotFound() {
    PricePolicyCreateRequestDto requestDto = new PricePolicyCreateRequestDto(
        TargetType.FEE, 1L, "Weekend Surcharge", "SAT, SUN", 1.2);

    when(cafeRepository.findById(cafe.getId())).thenReturn(Optional.empty());

    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        pricePolicyService.addPricePolicy(cafe.getId(), requestDto));

    assertThat(exception.getMessage()).isEqualTo(ErrorCode.CAFE_NOT_FOUND.getMessage());
  }

  @Test
  @DisplayName("가격 정책 조회 성공")
  void getPricePolicies_Success() {
    when(cafeRepository.existsById(cafe.getId())).thenReturn(true);
    when(pricePolicyRepository.findAllByCafeId(cafe.getId()))
        .thenReturn(List.of(pricePolicy));

    ListResponseDto<PricePolicyResponseDto> response = pricePolicyService.getPricePolicies(cafe.getId());

    assertThat(response.getMessage()).isEqualTo("카페 조회 성공");
    assertThat(response.getData()).hasSize(1);
    assertThat(response.getData().get(0).getTitle()).isEqualTo("Weekend Surcharge");
  }

  @Test
  @DisplayName("가격 정책 조회 실패 - 카페 존재하지 않음")
  void getPricePolicies_Fail_CafeNotFound() {
    when(cafeRepository.existsById(cafe.getId())).thenReturn(false);

    BusinessException exception = assertThrows(BusinessException.class, () ->
        pricePolicyService.getPricePolicies(cafe.getId()));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CAFE_NOT_FOUND);
  }

  @Test
  @DisplayName("가격 정책 수정 성공")
  void updatePricePolicy_Success() {
    PricePolicyUpdateRequestDto requestDto = new PricePolicyUpdateRequestDto(1L, "Weekday Discount", "MON-FRI", 1.0);

    when(cafeRepository.findById(cafe.getId())).thenReturn(Optional.of(cafe));
    when(pricePolicyRepository.findById(pricePolicy.getId())).thenReturn(Optional.of(pricePolicy));

    StatusDto response = pricePolicyService.updatePricePolicy(cafe.getId(), pricePolicy.getId(), requestDto);

    assertThat(response.getMessage()).isEqualTo("가격 정책 수정 성공");
    verify(pricePolicyRepository, times(1)).findById(pricePolicy.getId());
  }

  @Test
  @DisplayName("가격 정책 수정 실패 - 카페 존재하지 않음")
  void updatePricePolicy_Fail_CafeNotFound() {
    PricePolicyUpdateRequestDto requestDto = new PricePolicyUpdateRequestDto(1L, "Weekday Discount", "MON-FRI", 1.0);

    when(cafeRepository.findById(cafe.getId())).thenReturn(Optional.empty());

    BusinessException exception = assertThrows(BusinessException.class, () ->
        pricePolicyService.updatePricePolicy(cafe.getId(), pricePolicy.getId(), requestDto));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CAFE_NOT_FOUND);
  }

  @Test
  @DisplayName("가격 정책 삭제 성공")
  void deletePricePolicy_Success() {
    when(cafeRepository.findById(cafe.getId())).thenReturn(Optional.of(cafe));
    when(pricePolicyRepository.findById(pricePolicy.getId())).thenReturn(Optional.of(pricePolicy));

    pricePolicyService.deletePricePolicy(cafe.getId(), pricePolicy.getId());

    verify(pricePolicyRepository, times(1)).delete(pricePolicy);
  }

  @Test
  @DisplayName("가격 정책 삭제 실패 - 정책 존재하지 않음")
  void deletePricePolicy_Fail_PolicyNotFound() {
    when(cafeRepository.findById(cafe.getId())).thenReturn(Optional.of(cafe));
    when(pricePolicyRepository.findById(pricePolicy.getId())).thenReturn(Optional.empty());

    BusinessException exception = assertThrows(BusinessException.class, () ->
        pricePolicyService.deletePricePolicy(cafe.getId(), pricePolicy.getId()));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.PRICE_POLICY_NOT_FOUND);
  }
}