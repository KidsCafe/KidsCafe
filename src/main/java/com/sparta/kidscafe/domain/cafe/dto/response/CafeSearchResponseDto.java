package com.sparta.kidscafe.domain.cafe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CafeSearchResponseDto {

  private Long id;
  private String name;
  private String address;

}
