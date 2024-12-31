package com.sparta.kidscafe.domain.cafe.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CafeImageDeleteRequestDto {
  private Long cafeId;
  private List<Long> images;
}
