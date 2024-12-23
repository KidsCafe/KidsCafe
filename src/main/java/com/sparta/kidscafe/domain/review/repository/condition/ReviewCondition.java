package com.sparta.kidscafe.domain.review.repository.condition;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.sparta.kidscafe.domain.cafe.repository.condition.CafeSearchCondition;
import com.sparta.kidscafe.domain.review.entity.QReview;
import org.springframework.stereotype.Component;

@Component
public class ReviewCondition {

  private final QReview review = QReview.review;

  public BooleanExpression betweenAvgStar(CafeSearchCondition condition) {
    if (condition.getMinStar() == null || condition.getMaxStar() == null) {
      return null;
    }

    NumberExpression<Double> avgStar = Expressions
        .asNumber(review.star.avg())
        .coalesce(0.0);

    return avgStar.between(
        condition.getMinStar(),
        condition.getMaxStar()
    );
  }
}
