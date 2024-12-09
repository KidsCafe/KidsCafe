package com.sparta.kidscafe.domain.pricepolicy.dummy;

import com.sparta.kidscafe.common.enums.TargetType;
import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PricePolicyDummyTest {

    @Test
    @DisplayName("가격 정책 Dummy 데이터 생성 테스트")
    void createDummyPricePolicy() {
        PricePolicy pricePolicy = PricePolicy.builder()
                .targetType(TargetType.FEE)
                .targetId(1L)
                .title("주말 추가 요금")
                .dayType("월, 화, 수, 목, 금, 토, 일")
                .rate(10.00)
                .build();

        assertThat(pricePolicy.getTitle()).isEqualTo("주말 추가 요금");
        assertThat(pricePolicy.getRate()).isEqualTo(10.00);
    }
}
