package com.sparta.kidscafe.domain.cafe.repository.condition;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.sparta.kidscafe.common.util.GeoUtil;
import com.sparta.kidscafe.domain.cafe.entity.QCafe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CafeCondition {

  private final GeoUtil geoUtil;
  private final QCafe cafe = QCafe.cafe;

  public BooleanExpression likeName(String name) {
    if (!StringUtils.hasText(name)) {
      return null;
    }
    return cafe.name.contains(name);
  }

  public BooleanExpression eqUserId(Long userId) {
    if (userId == null || userId <= 0) {
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

  public BooleanExpression existCareService(Boolean careService) {
    if (careService == null) {
      return null;
    }

    if (careService) {
      return cafe.careService.isTrue();
    } else {
      return cafe.careService.isFalse();
    }
  }

  public BooleanExpression existSwimmingPool(Boolean swimmingPool) {
    if (swimmingPool == null) {
      return null;
    }

    if (swimmingPool) {
      return cafe.swimmingPool.isTrue();
    } else {
      return cafe.swimmingPool.isFalse();
    }
  }

  public BooleanExpression existClothesRental(Boolean clothesRental) {
    if (clothesRental == null) {
      return null;
    }

    if (clothesRental) {
      return cafe.clothesRental.isTrue();
    } else {
      return cafe.clothesRental.isFalse();
    }
  }

  public BooleanExpression existMonitoring(Boolean monitoring) {
    if (monitoring == null) {
      return null;
    }

    if (monitoring) {
      return cafe.monitoring.isTrue();
    } else {
      return cafe.monitoring.isFalse();
    }
  }

  public BooleanExpression existFeedingRoom(Boolean feedingRoom) {
    if (feedingRoom == null) {
      return null;
    }

    if (feedingRoom) {
      return cafe.feedingRoom.isTrue();
    } else {
      return cafe.feedingRoom.isFalse();
    }
  }

  public BooleanExpression existOutdoorPlayground(Boolean playground) {
    if (playground == null) {
      return null;
    }

    if (playground) {
      return cafe.outdoorPlayground.isTrue();
    } else {
      return cafe.outdoorPlayground.isFalse();
    }
  }

  public BooleanExpression existSafetyGuard(Boolean safetyGuard) {
    if (safetyGuard == null) {
      return null;
    }

    if (safetyGuard) {
      return cafe.safetyGuard.isTrue();
    } else {
      return cafe.safetyGuard.isFalse();
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
    innerBuilder.and(cafe.dayOff.contains(today).not());
    innerBuilder.and(cafe.openedAt.loe(currentTime));
    innerBuilder.and(cafe.closedAt.goe(currentTime));
    return innerBuilder.getValue();
  }

  public Predicate isOpening(LocalDateTime reservationDay) {
    if (reservationDay == null) {
      return null;
    }

    String korDay = reservationDay
        .getDayOfWeek()
        .getDisplayName(TextStyle.SHORT, Locale.KOREAN);

    BooleanBuilder innerBuilder = new BooleanBuilder();
    innerBuilder.and(cafe.dayOff.contains(korDay).not());
    innerBuilder.and(cafe.openedAt.loe(reservationDay.toLocalTime()));
    innerBuilder.and(cafe.closedAt.goe(reservationDay.toLocalTime()));
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

  public BooleanExpression existRestaurant(Boolean restaurant) {
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

  public BooleanExpression withInRadius(CafeSearchCondition condition) {
    Double lat = condition.getLat();
    Double lon = condition.getLon();
    Double radiusMeter = condition.getRadiusMeter();
    return withInRadius(lon, lat, radiusMeter);
  }

  public BooleanExpression withInRadius(Double lon, Double lat, Double radiusMeter) {
    if (geoUtil.validWktPoint(lon, lat)) {
      return null;
    }

    if (radiusMeter == null || radiusMeter <= 0) {
      return null;
    }

    return Expressions.booleanTemplate(
        "ST_Distance_Sphere({0}, ST_GeomFromText({1}, 4326)) <= {2}",
        cafe.location,
        geoUtil.convertPointToWkt(lon, lat),
        radiusMeter
    );
  }

  public NumberTemplate<Double> selectLon() {
    return Expressions.numberTemplate(Double.class, "ST_Longitude({0})", cafe.location);
  }

  public NumberTemplate<Double> selectLat() {
    return Expressions.numberTemplate(Double.class, "ST_Latitude({0})", cafe.location);
  }
}
