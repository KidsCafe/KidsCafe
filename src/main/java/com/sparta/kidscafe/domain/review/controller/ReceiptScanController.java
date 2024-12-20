package com.sparta.kidscafe.domain.review.controller;

import com.sparta.kidscafe.domain.review.dto.request.ReceiptScanRequestDto;
import com.sparta.kidscafe.domain.review.dto.response.ReceiptScanResponseDto;
import com.sparta.kidscafe.domain.review.service.ReceiptScanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReceiptScanController {

  private final ReceiptScanService receiptScanService;

  @PostMapping("/scan")
  public ResponseEntity<ReceiptScanResponseDto> scanReceipt(
      @RequestParam("receiptImage") MultipartFile receiptImage) {
    if (receiptImage == null || receiptImage.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(new ReceiptScanResponseDto(false, null, "업로드된 이미지가 비어있거나 존재하지 않습니다."));
    }

    try {
      ReceiptScanRequestDto requestDto = new ReceiptScanRequestDto();
      requestDto.setReceiptImage(receiptImage);

      ReceiptScanResponseDto response = receiptScanService.scanReceipt(requestDto);
      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest()
          .body(new ReceiptScanResponseDto(false, null, e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ReceiptScanResponseDto(false, null, "OCR 처리 중 오류가 발생했습니다."));
    }
  }
}
