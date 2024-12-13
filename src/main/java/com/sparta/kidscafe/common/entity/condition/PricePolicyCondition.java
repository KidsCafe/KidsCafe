package com.sparta.kidscafe.common.entity.condition;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.sparta.kidscafe.common.enums.TargetType;
import com.sparta.kidscafe.domain.pricepolicy.entity.QPricePolicy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class PricePolicyCondition {

  private final QPricePolicy pricePolicy = QPricePolicy.pricePolicy;

  public BooleanExpression eqTargetId(Long id) {
    if (id == null) {
      return null;
    }

    return pricePolicy.targetId.eq(id);
  }

  public BooleanExpression eqTargetType(TargetType targetType) {
    if (targetType == null) {
      return null;
    }

    return pricePolicy.targetType.eq(targetType);
  }

  public Predicate likeWorkDay(String dayType) {
    if (StringUtils.hasText(dayType)) {
      return null;
    }

    String[] workDays = dayType.split(", ");
    BooleanBuilder innerBuilder = new BooleanBuilder();
    for(String workDay : workDays) {
      innerBuilder.or(pricePolicy.dayType.contains(workDay));
    }

    return innerBuilder.getValue();
  }
}
