package com.sparta.kidscafe.dummy;

import com.sparta.kidscafe.common.enums.AgeGroup;
import com.sparta.kidscafe.common.util.TestUtil;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.fee.entity.Fee;

import java.util.ArrayList;
import java.util.List;

public class DummyFee {
  public static Fee createDummyFee(Cafe cafe, AgeGroup ageGroup) {
    int randomFee = TestUtil.getRandomPrice(3000, 30000);
    return Fee.builder()
        .cafe(cafe)
        .ageGroup(ageGroup)
        .fee(randomFee)
        .build();
  }

  public static List<Fee> createDummyFees(Cafe cafe) {
    List<Fee> fees = new ArrayList<>();
    for (AgeGroup ageGroup : AgeGroup.values()) {
      fees.add(createDummyFee(cafe, ageGroup));
    }
    return fees;
  }
}
