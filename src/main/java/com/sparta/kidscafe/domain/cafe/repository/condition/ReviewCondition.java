package com.sparta.kidscafe.domain.cafe.repository.condition;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.sparta.kidscafe.domain.cafe.dto.SearchCondition;
import com.sparta.kidscafe.domain.review.entity.QReview;
import org.springframework.stereotype.Component;

@Component
public class ReviewCondition {

  private final QReview review = QReview.review;

  public BooleanExpression betweenAvgStar(SearchCondition condition) {
    return review.star.avg().between(
        condition.getMinStar(),
        condition.getMaxStar()
    );
  }
}
