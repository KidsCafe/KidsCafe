package com.sparta.kidscafe.domain.lesson.repository.condition;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.sparta.kidscafe.domain.lesson.entity.QLesson;
import org.springframework.stereotype.Component;

@Component
public class LessonCondition {
  private final QLesson lesson = QLesson.lesson;

  public BooleanExpression selectExistLesson() {
    return new CaseBuilder()
        .when(lesson.cafe.id.countDistinct().goe(1))
        .then(true)
        .otherwise(false);
  }

  public BooleanExpression existLesson(Boolean existRoom) {
    if (existRoom == null || !existRoom) {
      return null;
    }

    return lesson.cafe.id.countDistinct().goe(1);
  }
}
