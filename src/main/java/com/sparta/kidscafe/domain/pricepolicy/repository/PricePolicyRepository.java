package com.sparta.kidscafe.domain.pricepolicy.repository;

import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PricePolicyRepository extends JpaRepository<PricePolicy, Long>, PricePolicyDslRepository {

  List<PricePolicy> findAllByCafeId(Long cafeId);

  List<PricePolicy> findAllByCafeIdAndTargetId(Long cafeId, Long targetId);

  boolean existsByCafeId(Long cafeId);

  List<PricePolicy> findByCafeIdAndDayTypeContains(Long cafeId, String dayType);
}
