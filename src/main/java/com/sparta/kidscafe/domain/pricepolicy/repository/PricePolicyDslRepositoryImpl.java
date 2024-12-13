package com.sparta.kidscafe.domain.pricepolicy.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.kidscafe.common.entity.condition.FeeCondition;
import com.sparta.kidscafe.common.entity.condition.PricePolicyCondition;
import com.sparta.kidscafe.common.entity.condition.RoomCondition;
import com.sparta.kidscafe.domain.fee.entity.QFee;
import com.sparta.kidscafe.domain.pricepolicy.dto.response.PricePolicyDto;
import com.sparta.kidscafe.domain.pricepolicy.dto.response.QPricePolicyDto;
import com.sparta.kidscafe.domain.pricepolicy.entity.QPricePolicy;
import com.sparta.kidscafe.domain.pricepolicy.searchcondition.PricePolicySearchCondition;
import com.sparta.kidscafe.domain.room.entity.QRoom;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PricePolicyDslRepositoryImpl implements PricePolicyDslRepository {

  private final JPAQueryFactory queryFactory;
  private final FeeCondition feeCondition;
  private final PricePolicyCondition pricePolicyCondition;

  private final QPricePolicy pricePolicy = QPricePolicy.pricePolicy;
  private final QRoom room = QRoom.room;
  private final QFee fee = new QFee("fee");

  @Override
  public PricePolicyDto findPricePolicyWithRoom(PricePolicySearchCondition condition) {
    return queryFactory.select(new QPricePolicyDto(
            room.price,
            pricePolicy.rate
        ))
        .from(pricePolicy)
        .leftJoin(room).on(room.cafe.id.eq(pricePolicy.cafe.id))
        .where(makeWhere(condition))
        .fetchFirst();
  }

  @Override
  public PricePolicyDto findPricePolicyWithFee(PricePolicySearchCondition condition) {
    return queryFactory.select(new QPricePolicyDto(
            fee.fee,
            pricePolicy.rate
        ))
        .from(pricePolicy)
        .leftJoin(fee).on(fee.cafe.id.eq(pricePolicy.cafe.id))
        .where(makeWhere(condition))
        .fetchFirst();
  }

  private BooleanBuilder makeWhere(PricePolicySearchCondition condition) {
    return new BooleanBuilder()
        .and(feeCondition.eqCafeId(condition.getCafeId()))
        .and(feeCondition.inAgeGroup(condition.getAgeGroup()))
        .and(pricePolicyCondition.eqTargetType(condition.getTargetType()))
        .and(pricePolicyCondition.eqTargetId(condition.getTargetId()))
        .and(pricePolicyCondition.likeWorkDay(condition.getWorking()));
  }
}
