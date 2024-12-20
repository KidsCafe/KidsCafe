package com.sparta.kidscafe.domain.cafe.dto.response;

import java.time.LocalTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class CafeSimpleResponseDto {
  private Long id;
  private String name;
  private double star;
  private Long reviewCount;
  private String dayOff;
  private String hyperLink;
  private LocalTime openedAt;
  private LocalTime closedAt;
}
