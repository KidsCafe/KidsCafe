package com.sparta.kidscafe.domain.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptScanResponseDto {
  private boolean isValid; // 영수증 검증 성공 여부
  private String storeName; // 가게 이름
  private String message; // 응답 메시지
}