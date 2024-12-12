package com.sparta.kidscafe.common.entity.condition;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.sparta.kidscafe.domain.cafe.dto.searchCondition.SearchCondition;
import com.sparta.kidscafe.domain.review.entity.QReview;
import org.springframework.stereotype.Component;

@Component
public class ReviewCondition {

  private final QReview review = QReview.review;

  public BooleanExpression betweenAvgStar(SearchCondition condition) {
    if(condition.getMinStar() == null || condition.getMaxStar() == null) {
      return null;
    }

    return review.star.avg().between(
        condition.getMinStar(),
        condition.getMaxStar()
    );
  }
}
