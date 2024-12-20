package com.sparta.kidscafe.domain.cafe.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeResponseDto;
import com.sparta.kidscafe.domain.cafe.dto.response.QCafeResponseDto;
import com.sparta.kidscafe.domain.cafe.entity.QCafe;
import com.sparta.kidscafe.domain.cafe.repository.condition.CafeCondition;
import com.sparta.kidscafe.domain.cafe.repository.condition.CafeSearchCondition;
import com.sparta.kidscafe.domain.cafe.repository.sort.CafeSearchSortBy;
import com.sparta.kidscafe.domain.fee.entity.QFee;
import com.sparta.kidscafe.domain.fee.repository.condition.FeeCondition;
import com.sparta.kidscafe.domain.lesson.entity.QLesson;
import com.sparta.kidscafe.domain.lesson.repository.condition.LessonCondition;
import com.sparta.kidscafe.domain.review.entity.QReview;
import com.sparta.kidscafe.domain.review.repository.condition.ReviewCondition;
import com.sparta.kidscafe.domain.room.entity.QRoom;
import com.sparta.kidscafe.domain.room.repository.condition.RoomCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@RequiredArgsConstructor
public class CafeDslRepositoryImpl implements CafeDslRepository {

  private final JPAQueryFactory queryFactory;
  private final RoomCondition roomCondition;
  private final LessonCondition lessonCondition;
  private final ReviewCondition reviewCondition;
  private final CafeCondition cafeCondition;
  private final FeeCondition feeCondition;

  private final QCafe cafe = QCafe.cafe;
  private final QRoom room = QRoom.room;
  private final QLesson lesson = QLesson.lesson;
  private final QReview review = QReview.review;
  private final QFee fee = new QFee("fee");

  @Override
  public CafeResponseDto findCafeById(Long id) {
    return baseCafeQuery()
        .where(cafe.id.eq(id))
        .groupBy(cafe.id)
        .fetchFirst();
  }

  @Override
  public Page<CafeResponseDto> findAllByCafe(CafeSearchCondition condition) {
    long cntTotal = searchCafeTotalCount(condition);
    if (cntTotal == 0) {
      return Page.empty();
    }

    JPAQuery<CafeResponseDto> query = baseCafeQuery()
        .leftJoin(fee).on(fee.cafe.eq(cafe))
        .where(makeWhere(condition))
        .groupBy(cafe.id)
        .having(makeHaving(condition))
        .orderBy(makeOrderBy(condition));

    List<CafeResponseDto> cafes = makePaging(query, condition).fetch();
    return new PageImpl<>(cafes, condition.getPageable(), cntTotal);
  }

  private long searchCafeTotalCount(CafeSearchCondition condition) {
    Long cntTotal = queryFactory
        .select(cafe.id.countDistinct())
        .from(cafe)
        .leftJoin(review).on(review.cafe.eq(cafe))
        .leftJoin(room).on(room.cafe.eq(cafe))
        .leftJoin(lesson).on(lesson.cafe.eq(cafe))
        .leftJoin(fee).on(fee.cafe.eq(cafe))
        .where(makeWhere(condition))
        .groupBy(cafe.id)
        .having(makeHaving(condition))
        .fetchFirst();
    return cntTotal == null ? 0 : cntTotal;
  }

  private JPAQuery<CafeResponseDto> baseCafeQuery() {
    return queryFactory.select(new QCafeResponseDto(
            cafe.id,
            cafe.name,
            cafe.address,
            cafeCondition.selectLon(),
            cafeCondition.selectLat(),
            cafe.size,
            review.star.avg(),
            review.id.countDistinct(),
            cafe.dayOff,
            cafe.multiFamily,
            cafe.parking,
            cafe.restaurant,
            cafe.careService,
            cafe.swimmingPool,
            cafe.clothesRental,
            cafe.monitoring,
            cafe.feedingRoom,
            cafe.outdoorPlayground,
            cafe.safetyGuard,
            roomCondition.selectExistRoom(),
            lessonCondition.selectExistLesson(),
            cafe.hyperlink,
            cafe.openedAt,
            cafe.closedAt))
        .from(cafe)
        .leftJoin(review).on(review.cafe.eq(cafe))
        .leftJoin(room).on(room.cafe.eq(cafe))
        .leftJoin(lesson).on(lesson.cafe.eq(cafe));
  }

  private BooleanBuilder makeWhere(CafeSearchCondition condition) {
    return new BooleanBuilder()
        .and(cafeCondition.likeName(condition.getName()))
        .and(cafeCondition.eqRegion(condition.getRegion()))
        .and(cafeCondition.loeSize(condition.getSize()))
        .and(feeCondition.ageGroup(condition))
        .and(cafeCondition.isOpening(condition.getOpening()))
        .and(cafeCondition.multiFamily(condition.getMultiFamily()))
        .and(cafeCondition.parking(condition.getParking()))
        .and(cafeCondition.existRestaurant(condition.getExistRestaurant()))
        .and(cafeCondition.existCareService(condition.getExistCareService()))
        .and(cafeCondition.existSwimmingPool(condition.getExistSwimmingPool()))
        .and(cafeCondition.existClothesRental(condition.getExistClothesRental()))
        .and(cafeCondition.existMonitoring(condition.getExistMonitoring()))
        .and(cafeCondition.existFeedingRoom(condition.getExistFeedingRoom()))
        .and(cafeCondition.existOutdoorPlayground(condition.getExistOutdoorPlayground()))
        .and(cafeCondition.existSafetyGuard(condition.getExistSafetyGuard()))
        .and(feeCondition.adultPrice(condition.getAdultPrice()))
        .and(cafeCondition.goeOpenedAt(condition.getOpenedAt()))
        .and(cafeCondition.loeClosedAt(condition.getClosedAt()))
        .and(cafeCondition.eqUserId(condition.getUserId()))
        .and(cafeCondition.withInRadius(condition));
  }

  private BooleanBuilder makeHaving(CafeSearchCondition condition) {
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(reviewCondition.betweenAvgStar(condition));
    builder.and(roomCondition.existRoom(condition.getExistRoom()));
    builder.and(lessonCondition.existLesson(condition.getExistLesson()));
    return builder;
  }

  private OrderSpecifier<?> makeOrderBy(CafeSearchCondition condition) {
    Order order = condition.isAsc() ? Order.ASC : Order.DESC;
    Expression<?> expression = makeOrderBy(condition.getSortBy());
    if (expression instanceof ComparableExpressionBase<?> orderExpression) {
      return new OrderSpecifier<>(order, orderExpression);
    }

    return new OrderSpecifier<>(Order.ASC, Expressions.constant(1));
  }

  private Expression<?> makeOrderBy(CafeSearchSortBy sortBy) {
    return switch (sortBy) {
      case REVIEW_COUNT -> review.id.count();
      case REVIEW_AVG -> review.star.avg();
      case ROOM_EXIST -> roomCondition.selectExistRoom();
      default -> cafe.name;
    };
  }

  private JPAQuery<CafeResponseDto> makePaging(JPAQuery<CafeResponseDto> query, CafeSearchCondition condition) {
    if (condition.getPageable() != null) {
      query
          .limit(condition.getPageable().getPageSize())
          .offset(condition.getPageable().getOffset());
    }
    return query;
  }
}
