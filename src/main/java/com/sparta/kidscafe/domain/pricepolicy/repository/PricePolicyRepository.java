package com.sparta.kidscafe.domain.pricepolicy.repository;

import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PricePolicyRepository extends JpaRepository<PricePolicy, Long> {
    List<PricePolicy> findAllByCafeId(Long cafeId);
}
