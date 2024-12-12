package com.sparta.kidscafe.domain.cafe.dto.request;

import com.sparta.kidscafe.domain.cafe.enums.SearchSortBy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CafeSearchPageRequestDto {

  private int page;
  private int pageSize;
  private SearchSortBy sortBy;
  private boolean asc;

  public Pageable getPageable() {
    if(page < 1) {
      return null;
    }

    return PageRequest.of(page - 1, pageSize);
  }
}
