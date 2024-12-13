package com.sparta.kidscafe.domain.pricepolicy.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PricePolicyDto {
  private int price;
  private double rate;

  @QueryProjection
  public PricePolicyDto(int price, double rate) {
    this.price = price;
    this.rate = rate;
  }
}
