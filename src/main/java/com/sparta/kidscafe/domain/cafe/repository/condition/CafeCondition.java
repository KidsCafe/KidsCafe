package com.sparta.kidscafe.domain.cafe.repository.condition;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.sparta.kidscafe.common.util.GeoUtil;
import com.sparta.kidscafe.common.util.condition.BaseCondition;
import com.sparta.kidscafe.domain.cafe.entity.QCafe;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CafeCondition extends BaseCondition {

  private final GeoUtil geoUtil;
  private final QCafe cafe = QCafe.cafe;

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

  public BooleanExpression withInRadius(CafeSearchCondition condition) {
    Double lat = condition.getLat();
    Double lon = condition.getLon();
    Double radiusMeter = condition.getRadiusMeter();
    return withInRadius(lon, lat, radiusMeter);
  }

  public BooleanExpression withInRadius(Double lon, Double lat, Double radiusMeter) {
    if(geoUtil.validWktPoint(lon, lat)) {
      return null;
    }

    if(radiusMeter == null || radiusMeter <= 0) {
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
