package com.sparta.kidscafe.domain.cafe.repository;

import com.sparta.kidscafe.domain.cafe.repository.condition.CafeCafeSearchCondition;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeResponseDto;
import org.springframework.data.domain.Page;

public interface CafeDslRepository {

  CafeResponseDto findCafeById(Long id);

  Page<CafeResponseDto> findAllByCafe(CafeCafeSearchCondition condition);
}
