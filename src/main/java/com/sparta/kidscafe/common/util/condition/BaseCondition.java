package com.sparta.kidscafe.common.util.condition;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.types.dsl.TimePath;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Pageable;
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

  public <T extends Number & Comparable<T>> BooleanExpression loe(NumberPath<T> path, T value) {
    if (value == null) {
      return null;
    }

    return path.loe(value);
  }

  public <T extends Comparable<T>> BooleanExpression loe(TimePath<T> path, T value) {
    if (value == null) {
      return null;
    }

    return path.loe(value);
  }

  public <T extends Number & Comparable<T>> BooleanExpression goe(NumberPath<T> path, T value) {
    if (value == null) {
      return null;
    }

    return path.goe(value);
  }

  public <T extends Comparable<T>> BooleanExpression goe(TimePath<T> path, T value) {
    if (value == null) {
      return null;
    }

    return path.goe(value);
  }

  public <T> JPAQuery<T> makePaging(JPAQuery<T> query, Pageable pageable) {
    if (pageable != null) {
      query
          .limit(pageable.getPageSize())
          .offset(pageable.getOffset());
    }
    return query;
  }
}
