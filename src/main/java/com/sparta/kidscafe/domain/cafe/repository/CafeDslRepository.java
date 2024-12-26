package com.sparta.kidscafe.domain.cafe.repository;

import com.sparta.kidscafe.domain.cafe.dto.response.CafeSimpleResponseDto;
import com.sparta.kidscafe.domain.cafe.repository.condition.CafeSearchCondition;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeResponseDto;
import org.springframework.data.domain.Page;

public interface CafeDslRepository {

  CafeResponseDto findCafeById(Long id);

  Page<CafeSimpleResponseDto> findAllByCafeSimple(CafeSearchCondition condition);

  Page<CafeResponseDto> findAllByCafe(CafeSearchCondition condition);

  long searchTotalCount(CafeSearchCondition condition);
}
