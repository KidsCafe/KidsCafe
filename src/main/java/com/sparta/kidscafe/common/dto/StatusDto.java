package com.sparta.kidscafe.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * [등록/수정]에 사용할 수 있는 Dto
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class StatusDto {
  private int status;
  private String message;
}
