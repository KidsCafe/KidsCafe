package com.sparta.kidscafe.domain.pricepolicy.repository;

import com.sparta.kidscafe.domain.pricepolicy.searchcondition.PricePolicySearchCondition;

import java.util.List;

public interface PricePolicyDslRepository {
    List<Double> findPricePolicyWithRoom(PricePolicySearchCondition condition);

    List<Double> findPricePolicyWithFee(PricePolicySearchCondition condition);
}
