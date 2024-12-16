package com.sparta.kidscafe.common.entity.condition;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.sparta.kidscafe.domain.cafe.entity.QCafe;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

  public BooleanExpression eqUserId(Long userId) {
    if(userId == null || userId <= 0) {
      return null;
    }
    return cafe.user.id.eq(userId);
  }

  public BooleanExpression eqRegion(String region) {
    if (region == null || region.isEmpty()) {
      return null;
    }
    return cafe.region.eq(region);
  }

  public BooleanExpression loeSize(Integer size) {
    if (size == null || size < 0) {
      return null;
    }
    return cafe.size.loe(size);
  }

  public BooleanExpression parking(Boolean parking) {
    if (parking == null) {
      return null;
    }

    if (parking) {
      return cafe.parking.isTrue();
    } else {
      return cafe.parking.isFalse();
    }
  }

  public Predicate isOpening(Boolean opening) {
    if (opening == null || !opening) {
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

  public Predicate notHoliday(LocalDateTime reservationDay) {
    BooleanBuilder innerBuilder = new BooleanBuilder();
    String reservationDayByKorean = LocalDate
        .now()
        .getDayOfWeek()
        .getDisplayName(TextStyle.SHORT, Locale.KOREAN);
    innerBuilder.and(cafe.dayOff.notLike("%"+reservationDayByKorean+"%"));
    return innerBuilder.getValue();
  }

  public Predicate dayOff(String dayOff) {
    BooleanBuilder innerBuilder = new BooleanBuilder();
    String[] days = dayOff.split(", ");
    for (String day : days) {
      innerBuilder.and(cafe.dayOff.contains(day));
    }
    return innerBuilder.getValue();
  }

  public BooleanExpression restaurantExists(Boolean restaurant) {
    if (restaurant == null) {
      return null;
    }

    if (restaurant) {
      return cafe.restaurant.isTrue();
    } else {
      return cafe.restaurant.isFalse();
    }
  }

  public BooleanExpression multiFamily(Boolean multiFamily) {
    if (multiFamily == null) {
      return null;
    }

    if (multiFamily) {
      return cafe.multiFamily.isTrue();
    } else {
      return cafe.multiFamily.isFalse();
    }
  }

  public BooleanExpression goeOpenedAt(LocalTime openedAt) {
    // goe 크거나 같음, loe 작거나 같음
    if (openedAt == null) {
      return null;
    }
    return cafe.openedAt.goe(openedAt);
  }

  public BooleanExpression loeOpenedAt(LocalTime openedAt) {
    // goe 크거나 같음, loe 작거나 같음
    if (openedAt == null) {
      return null;
    }
    return cafe.openedAt.loe(openedAt);
  }

  public BooleanExpression goeClosedAt(LocalTime closedAt) {
    if (closedAt == null) {
      return null;
    }
    return cafe.closedAt.goe(closedAt);
  }

  public BooleanExpression loeClosedAt(LocalTime closedAt) {
    if (closedAt == null) {
      return null;
    }
    return cafe.closedAt.loe(closedAt);
  }
}
