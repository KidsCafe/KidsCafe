package com.sparta.kidscafe.domain.cafe.repository.condition;

import com.sparta.kidscafe.common.entity.sort.CafeSearchSortBy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Pageable;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CafeSearchPageCondition {

  private Pageable pageable;
  private CafeSearchSortBy sortBy;
  private boolean asc;
}
