package com.sparta.kidscafe.domain.pricepolicy.service;

import com.sparta.kidscafe.domain.pricepolicy.dto.request.PricePolicyCreateRequestDto;
import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import com.sparta.kidscafe.domain.pricepolicy.repository.PricePolicyRepository;
import com.sparta.kidscafe.common.enums.TargetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class PricePolicyServiceTest {

    @Autowired
    private PricePolicyService pricePolicyService;

    @MockBean
    private PricePolicyRepository pricePolicyRepository;

    private PricePolicyCreateRequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = PricePolicyCreateRequestDto.builder()
                .targetType(TargetType.FEE)
                .targetId(1L)
                .title("주말 추가 요금")
                .dayType("월, 화, 수, 목, 금, 토, 일")
                .rate(10.0)
                .build();
    }

    @Test
    @DisplayName("가격 정책 추가 - 성공")
    void addPricePolicySuccess() {
        // Given
        Mockito.when(pricePolicyRepository.save(Mockito.any(PricePolicy.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        pricePolicyService.addPricePolicy(1L, requestDto);

        // Then
        ArgumentCaptor<PricePolicy> captor = ArgumentCaptor.forClass(PricePolicy.class);
        verify(pricePolicyRepository, times(1)).save(captor.capture());
        PricePolicy savedPolicy = captor.getValue();

        assertThat(savedPolicy.getTargetType()).isEqualTo(requestDto.getTargetType());
        assertThat(savedPolicy.getTargetId()).isEqualTo(requestDto.getTargetId());
        assertThat(savedPolicy.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(savedPolicy.getDayType()).isEqualTo(requestDto.getDayType());
        assertThat(savedPolicy.getRate()).isEqualTo(requestDto.getRate());
    }
}