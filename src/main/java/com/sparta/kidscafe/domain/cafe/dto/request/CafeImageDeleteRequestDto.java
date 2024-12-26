package com.sparta.kidscafe.domain.cafe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CafeImageDeleteRequestDto {
  private Long cafeId;
  private List<Long> images;
}
