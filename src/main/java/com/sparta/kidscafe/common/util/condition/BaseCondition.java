package com.sparta.kidscafe.common.util.condition;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.util.StringUtils;

public class BaseCondition {

  public BooleanExpression isTrue(BooleanPath path, Boolean isTrue) {
    if (isTrue == null) {
      return null;
    }

    if (isTrue) {
      return path.isTrue();
    } else {
      return path.isFalse();
    }
  }

  public BooleanExpression eq(StringPath path, String value) {
    if (!StringUtils.hasText(value)) {
      return null;
    }

    return path.eq(value);
  }

  public <T extends Number & Comparable<?>> BooleanExpression eq(NumberPath<T> path, T value) {
    if (value == null) {
      return null;
    }

    return path.eq(value);
  }

  public BooleanExpression contains(StringPath path, String value) {
    if (!StringUtils.hasText(value)) {
      return null;
    }

    return path.contains(value);
  }

  public <T extends Number & Comparable<?>> BooleanExpression loe(NumberPath<?> path, T value) {
    if (value == null) {
      return null;
    }

    return path.loe(value);
  }

  public <T extends Number & Comparable<?>> BooleanExpression goe(NumberPath<?> path, T value) {
    if (value == null) {
      return null;
    }

    return path.goe(value);
  }
}
