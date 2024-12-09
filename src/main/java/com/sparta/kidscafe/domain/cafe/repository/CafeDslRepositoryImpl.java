package com.sparta.kidscafe.domain.cafe.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.kidscafe.domain.cafe.dto.SearchCondition;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeResponseDto;
import com.sparta.kidscafe.domain.cafe.dto.response.QCafeResponseDto;
import com.sparta.kidscafe.domain.cafe.entity.QCafe;
import com.sparta.kidscafe.domain.cafe.enums.SearchSortBy;
import com.sparta.kidscafe.domain.cafe.repository.condition.CafeCondition;
import com.sparta.kidscafe.domain.cafe.repository.condition.FeeCondition;
import com.sparta.kidscafe.domain.cafe.repository.condition.ReviewCondition;
import com.sparta.kidscafe.domain.cafe.repository.condition.RoomCondition;
import com.sparta.kidscafe.domain.fee.entity.QFee;
import com.sparta.kidscafe.domain.review.entity.QReview;
import com.sparta.kidscafe.domain.room.entity.QRoom;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@RequiredArgsConstructor
public class CafeDslRepositoryImpl implements CafeDslRepository {

  private final JPAQueryFactory queryFactory;
  private final RoomCondition roomCondition;
  private final ReviewCondition reviewCondition;
  private final CafeCondition cafeCondition;
  private final FeeCondition feeCondition;
  private final QCafe cafe = QCafe.cafe;
  private final QRoom room = QRoom.room;
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
  public Page<CafeResponseDto> findAllByCafe(SearchCondition condition) {
    long cntTotal = searchCafeTotalCount(condition);
    if (cntTotal == 0) {
      return Page.empty();
    }

    List<CafeResponseDto> cafes = baseCafeQuery()
        .leftJoin(fee).on(fee.cafe.eq(cafe))
        .where(makeWhere(condition))
        .groupBy(cafe.id)
        .having(makeHaving(condition))
        .limit(condition.getPageable().getPageSize())
        .offset(condition.getPageable().getOffset())
        .fetch();
    return new PageImpl<>(cafes, condition.getPageable(), cntTotal);
  }

  private long searchCafeTotalCount(SearchCondition condition) {
    Long cntTotal = queryFactory
        .select(cafe.id.countDistinct())
        .from(cafe)
        .leftJoin(review).on(review.cafe.eq(cafe))
        .leftJoin(room).on(room.cafe.eq(cafe))
        .leftJoin(fee).on(fee.cafe.eq(cafe))
        .where(makeWhere(condition))
        .groupBy(cafe.id)
        .having(makeHaving(condition))
        .orderBy(makeOrderBy(condition)).fetchFirst();
    return cntTotal == null ? 0 : cntTotal;
  }

  private JPAQuery<CafeResponseDto> baseCafeQuery() {
    return queryFactory.select(new QCafeResponseDto(
            cafe.id,
            cafe.name,
            cafe.address,
            cafe.size,
            review.star.avg(),
            review.id.countDistinct(),
            cafe.dayOff,
            cafe.multiFamily,
            roomCondition.existRoom(),
            cafe.parking,
            cafe.restaurant,
            cafe.hyperlink,
            cafe.openedAt,
            cafe.closedAt))
        .from(cafe)
        .leftJoin(review).on(review.cafe.eq(cafe))
        .leftJoin(room).on(room.cafe.eq(cafe));
  }

  private BooleanBuilder makeWhere(SearchCondition condition) {
    return new BooleanBuilder()
        .and(cafeCondition.likeName(condition.getName()))
        .and(cafeCondition.eqRegion(condition.getRegion()))
        .and(cafeCondition.loeSize(condition.getSize()))
        .and(feeCondition.ageGroup(condition))
        .and(cafeCondition.parking(condition.isParking()))
        .and(cafeCondition.isOpening(condition.isOpening()))
        .and(cafeCondition.restaurantExists(condition.isExistRestaurant()))
        .and(feeCondition.adultPrice(condition.isAdultPrice()))
        .and(cafeCondition.multiFamily(condition.isMultiFamily()))
        .and(cafeCondition.goeOpenedAt(condition.getOpenedAt()))
        .and(cafeCondition.loeClosedAt(condition.getClosedAt()));
  }

  private BooleanBuilder makeHaving(SearchCondition condition) {
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(reviewCondition.betweenAvgStar(condition));
    builder.and(roomCondition.existRoom(condition.isExistRoom()));
    return builder;
  }

  private OrderSpecifier<?> makeOrderBy(SearchCondition condition) {
    Order order = condition.isAsc() ? Order.ASC : Order.DESC;
    Expression<?> expression = makeOrderBy(condition.getSortBy());
    if (expression instanceof ComparableExpressionBase<?> orderExpression) {
      return new OrderSpecifier<>(order, orderExpression);
    } else {
      return new OrderSpecifier<>(order, cafe.name);
    }
  }

  private Expression<?> makeOrderBy(SearchSortBy sortBy) {
    return switch (sortBy) {
      case REVIEW_COUNT -> review.id.count();
      case REVIEW_AVG -> review.star.avg();
      case ROOM_EXIST -> roomCondition.existRoom();
      default -> cafe.name;
    };
  }
}
