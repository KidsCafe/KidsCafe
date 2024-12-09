package com.sparta.kidscafe.domain.cafe.repository.condition;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.sparta.kidscafe.common.enums.AgeGroup;
import com.sparta.kidscafe.domain.cafe.dto.SearchCondition;
import com.sparta.kidscafe.domain.fee.entity.QFee;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class FeeCondition {

  private final QFee fee = new QFee("fee");

  public Predicate ageGroup(SearchCondition condition) {
    if (StringUtils.hasText(condition.getAgeGroup())) {
      return null;
    }

    BooleanBuilder builder = new BooleanBuilder();
    if (!StringUtils.hasText(condition.getAgeGroup())) {
      builder.and(eqAgeGroup(condition.getAgeGroup()));
      builder.and(betweenPrice(condition.getMinPrice(), condition.getMaxPrice()));
    }
    return builder.getValue();
  }

  public BooleanExpression eqAgeGroup(String ageGroup) {
    if (ageGroup == null || ageGroup.isEmpty()) {
      return null;
    }
    return fee.ageGroup.eq(AgeGroup.getAgeGroup(ageGroup));
  }

  public BooleanExpression betweenPrice(int minPrice, int maxPrice) {
    return fee.fee.between(minPrice, maxPrice);
  }

  public BooleanExpression adultPrice(boolean isAdultPrice) {
    if (!isAdultPrice) {
      return null;
    }
    return fee.ageGroup.notIn(Arrays.asList(AgeGroup.BABY, AgeGroup.TEENAGER));
  }
}
