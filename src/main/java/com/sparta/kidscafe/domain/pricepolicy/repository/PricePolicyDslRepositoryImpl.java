package com.sparta.kidscafe.domain.pricepolicy.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.kidscafe.domain.fee.repository.condition.FeeCondition;
import com.sparta.kidscafe.domain.pricepolicy.repository.condition.PricePolicyCondition;
import com.sparta.kidscafe.domain.fee.entity.QFee;
import com.sparta.kidscafe.domain.pricepolicy.entity.QPricePolicy;
import com.sparta.kidscafe.domain.pricepolicy.searchcondition.PricePolicySearchCondition;
import com.sparta.kidscafe.domain.room.entity.QRoom;
import java.util.List;
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
  public List<Double> findPricePolicyWithRoom(PricePolicySearchCondition condition) {
    return queryFactory.select(pricePolicy.rate)
        .from(room)
        .leftJoin(pricePolicy).on(pricePolicy.targetId.eq(room.id))
        .where(makeWhere(condition))
        .fetch();
  }

  @Override
  public List<Double> findPricePolicyWithFee(PricePolicySearchCondition condition) {
    return queryFactory.select(pricePolicy.rate)
        .from(fee)
        .leftJoin(pricePolicy).on(pricePolicy.targetId.eq(fee.id))
        .where(makeWhere(condition))
        .fetch();
  }

  private BooleanBuilder makeWhere(PricePolicySearchCondition condition) {
    return new BooleanBuilder()
        .and(feeCondition.eqCafeId(condition.getCafeId()))
        .and(pricePolicyCondition.eqTargetType(condition.getTargetType()))
        .and(pricePolicyCondition.eqTargetId(condition.getTargetId()))
        .and(pricePolicyCondition.likeWorkDay(condition.getWorking()));
  }
}
