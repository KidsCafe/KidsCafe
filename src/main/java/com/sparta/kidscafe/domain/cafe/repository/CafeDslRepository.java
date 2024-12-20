package com.sparta.kidscafe.domain.cafe.repository;

import com.sparta.kidscafe.domain.cafe.dto.response.CafeResponseDto;
import com.sparta.kidscafe.domain.cafe.repository.condition.CafeSearchCondition;
import org.springframework.data.domain.Page;

public interface CafeDslRepository {

  CafeResponseDto findCafeById(Long id);

  Page<CafeResponseDto> findAllByCafe(CafeSearchCondition condition);
}
