package com.sparta.kidscafe.domain.cafe.repository;

import com.sparta.kidscafe.domain.cafe.dto.SearchCondition;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeResponseDto;
import org.springframework.data.domain.Page;

public interface CafeDslRepository {
  Page<CafeResponseDto> searchCafe(SearchCondition condition);
}
