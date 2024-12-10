package com.sparta.kidscafe.domain.cafe.repository.condition;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.sparta.kidscafe.domain.cafe.entity.QCafe;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CafeCondition {

  private final QCafe cafe = QCafe.cafe;

  public BooleanExpression likeName(String name) {
    if (!StringUtils.hasText(name)) {
      return null;
    }

    return cafe.name.contains(name);
  }

  public BooleanExpression eqRegion(String region) {
    if (region == null || region.isEmpty()) {
      return null;
    }
    return cafe.region.eq(region);
  }

  public BooleanExpression loeSize(int size) {
    if (size < 0) {
      return null;
    }
    return cafe.size.loe(size);
  }

  public BooleanExpression parking(boolean parking) {
    if (parking) {
      return cafe.parking.isTrue();
    } else {
      return cafe.parking.isFalse();
    }
  }

  public Predicate isOpening(boolean opening) {
    if (!opening) {
      return null;
    }

    LocalTime currentTime = LocalTime.now();
    String today = LocalDate
        .now()
        .getDayOfWeek()
        .getDisplayName(TextStyle.SHORT, Locale.KOREAN);

    BooleanBuilder innerBuilder = new BooleanBuilder();
    innerBuilder.and(cafe.dayOff.contains(today));
    innerBuilder.and(cafe.openedAt.goe(currentTime));
    innerBuilder.and(cafe.openedAt.loe(currentTime));
    return innerBuilder.getValue();
  }

  public Predicate dayOff(String dayOff) {
    BooleanBuilder innerBuilder = new BooleanBuilder();
    String[] days = dayOff.split(", ");
    for (String day : days) {
      innerBuilder.and(cafe.dayOff.eq(day));
    }
    return innerBuilder.getValue();
  }

  public BooleanExpression restaurantExists(boolean restaurant) {
    if (restaurant) {
      return cafe.restaurant.isTrue();
    } else {
      return cafe.restaurant.isFalse();
    }
  }

  public BooleanExpression multiFamily(boolean multiFamily) {
    if (multiFamily) {
      return cafe.multiFamily.isTrue();
    } else {
      return cafe.multiFamily.isFalse();
    }
  }

  public BooleanExpression goeOpenedAt(LocalTime openedAt) {
    if (openedAt == null) {
      return null;
    }
    return cafe.openedAt.goe(openedAt);
  }

  public BooleanExpression loeClosedAt(LocalTime closedAt) {
    if (closedAt == null) {
      return null;
    }
    return cafe.closedAt.loe(closedAt);
  }
}
