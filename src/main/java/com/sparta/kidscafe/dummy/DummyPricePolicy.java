package com.sparta.kidscafe.dummy;

import com.sparta.kidscafe.common.enums.TargetType;
import com.sparta.kidscafe.common.util.TestUtil;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.fee.entity.Fee;
import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import com.sparta.kidscafe.domain.room.entity.Room;
import java.util.ArrayList;
import java.util.List;

public class DummyPricePolicy {
  public static PricePolicy createDummyPricePolicy(Cafe cafe, TargetType targetType, Long targetId) {
    String randomTitle = TestUtil.getRandomString(15);
    String randomDayType = TestUtil.getRandomDayOff();
    double randomRate = TestUtil.getRandomDouble(0, 2);
    return PricePolicy.builder()
        .cafe(cafe)
        .targetType(targetType)
        .targetId(targetId)
        .title(randomTitle)
        .dayType(randomDayType)
        .rate(randomRate)
        .build();
  }

  public static PricePolicy createDummyPricePolicy(Cafe cafe, Fee fee) {
    return createDummyPricePolicy(cafe, TargetType.FEE, fee.getId());
  }

  public static PricePolicy createDummyPricePolicy(Cafe cafe, Room room) {
    return createDummyPricePolicy(cafe, TargetType.ROOM, room.getId());
  }

  public static List<PricePolicy> createDummyPricePoliciesByRoom(Cafe cafe, List<Room> rooms) {
    List<PricePolicy> policies = new ArrayList<>();
    for (Room room : rooms) {
      policies.add(createDummyPricePolicy(cafe, room));
    }
    return policies;
  }

  public static List<PricePolicy> createDummyPricePoliciesByFee(Cafe cafe, List<Fee> fees) {
    List<PricePolicy> policies = new ArrayList<>();
    for (Fee fee : fees) {
      policies.add(createDummyPricePolicy(cafe, fee));
    }
    return policies;
  }
}
