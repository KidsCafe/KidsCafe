package com.sparta.kidscafe.common.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class StatusDto {
  private int status;
  private String message;
}
