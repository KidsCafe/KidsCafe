package com.sparta.kidscafe.domain.review.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class ReceiptScanRequestDto {

  @NotNull(message = "영수증 이미지는 필수입니다.")
  private MultipartFile receiptImage; // 업로드된 영수증 이미지

  // 추가적으로 영수증 이미지의 타입을 검증하고 싶다면 별도 검증 로직 추가 가능
  public boolean isValidImageType() {
    if (receiptImage == null || receiptImage.isEmpty()) {
      return false;
    }

    String contentType = receiptImage.getContentType();
    return contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png"));
  }
}
