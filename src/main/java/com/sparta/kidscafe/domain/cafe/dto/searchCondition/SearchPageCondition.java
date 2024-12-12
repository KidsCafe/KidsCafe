package com.sparta.kidscafe.domain.cafe.dto.searchCondition;

import com.sparta.kidscafe.domain.cafe.enums.SearchSortBy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Pageable;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SearchPageCondition {

  private Pageable pageable;
  private SearchSortBy sortBy;
  private boolean asc;
}