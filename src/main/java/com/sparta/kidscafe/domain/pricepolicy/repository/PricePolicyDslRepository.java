package com.sparta.kidscafe.domain.pricepolicy.repository;

import com.sparta.kidscafe.domain.pricepolicy.dto.response.PricePolicyDto;
import com.sparta.kidscafe.domain.pricepolicy.searchcondition.PricePolicySearchCondition;

public interface PricePolicyDslRepository {
  PricePolicyDto findPricePolicyWithRoom(PricePolicySearchCondition condition);

  PricePolicyDto findPricePolicyWithFee(PricePolicySearchCondition condition);
}
