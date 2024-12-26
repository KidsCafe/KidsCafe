package com.sparta.kidscafe.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

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

  public static StatusDto createStatusDto(HttpStatus status, String message) {
    return StatusDto.builder()
        .status(status.value())
        .message(message)
        .build();
  }
}
