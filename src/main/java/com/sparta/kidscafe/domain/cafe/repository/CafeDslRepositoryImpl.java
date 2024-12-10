package com.sparta.kidscafe.domain.cafe.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.kidscafe.domain.cafe.dto.SearchCondition;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeResponseDto;
import com.sparta.kidscafe.domain.cafe.dto.response.QCafeResponseDto;
import com.sparta.kidscafe.domain.cafe.entity.QCafe;
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
  public Page<CafeResponseDto> searchCafe(SearchCondition condition) {
    long cntTotal = searchCafeTotalCount(condition);
    if (cntTotal == 0) {
      return Page.empty();
    }

    List<CafeResponseDto> cafes = queryFactory.select(new QCafeResponseDto(
            cafe.id,
            cafe.name,
            cafe.address,
            cafe.size,
            review.star.avg(),
            review.id.countDistinct(),
            roomCondition.existRoom(),
            cafe.parking,
            cafe.restaurant,
            cafe.openedAt,
            cafe.closedAt))
        .from(cafe)
        .leftJoin(review).on(review.cafe.eq(cafe))
        .leftJoin(room).on(room.cafe.eq(cafe))
        .leftJoin(fee).on(fee.cafe.eq(cafe))
        .where(makeWhere(condition))
        .groupBy(cafe.id)
        .having(makeHaving(condition))
        .orderBy(makeOrderBy(condition))
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

  private BooleanBuilder makeWhere(SearchCondition condition) {
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(cafeCondition.likeName(condition.getName()));
    builder.and(cafeCondition.eqRegion(condition.getRegion()));
    builder.and(cafeCondition.loeSize(condition.getSize()));
    builder.and(feeCondition.ageGroup(condition));
    builder.and(cafeCondition.parking(condition.isParking()));
    builder.and(cafeCondition.isOpening(condition.isOpening()));
    builder.and(cafeCondition.restaurantExists(condition.isExistRestaurant()));
    builder.and(feeCondition.adultPrice(condition.isAdultPrice()));
    builder.and(cafeCondition.multiFamily(condition.isMultiFamily()));
    builder.and(cafeCondition.goeOpenedAt(condition.getOpenedAt()));
    builder.and(cafeCondition.loeClosedAt(condition.getClosedAt()));
    return builder;
  }

  private BooleanBuilder makeHaving(SearchCondition condition) {
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(reviewCondition.betweenAvgStar(condition));
    builder.and(roomCondition.existRoom(condition.isExistRoom()));
    return builder;
  }

  private OrderSpecifier<?> makeOrderBy(SearchCondition condition) {
    boolean isAsc = condition.isAsc();
    return switch (condition.getSortBy()) {
      case REVIEW_COUNT -> isAsc ? review.id.count().asc() : review.id.count().desc();
      case REVIEW_AVG -> isAsc ? review.star.avg().asc() : review.star.avg().desc();
      case ROOM_EXIST -> isAsc ? roomCondition.existRoom().asc() :
          roomCondition.existRoom().desc();
      default -> new OrderSpecifier<>(Order.ASC, cafe.name);
    };
  }
}
