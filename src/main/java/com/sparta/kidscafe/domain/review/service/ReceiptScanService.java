package com.sparta.kidscafe.domain.review.service;

import com.sparta.kidscafe.domain.review.dto.request.ReceiptScanRequestDto;
import com.sparta.kidscafe.domain.review.dto.response.ReceiptScanResponseDto;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class ReceiptScanService {

  private static final String TESSDATA_PATH = "/usr/share/tesseract-ocr/4.00/tessdata"; // Tesseract 데이터 경로
  private static final String DEFAULT_LANGUAGE = "eng"; // 기본 OCR 언어
  private static final String SECONDARY_LANGUAGE = "kor"; // 추가 OCR 언어 (한국어)

  public ReceiptScanResponseDto scanReceipt(ReceiptScanRequestDto requestDto) {
    MultipartFile receiptImage = requestDto.getReceiptImage();

    // 이미지 검증
    validateImage(receiptImage);

    try {
      // MultipartFile -> File 변환
      File tempFile = convertToFile(receiptImage);

      // OCR 수행
      String extractedText = performOcr(tempFile);

      // 텍스트 검증 및 분석
      boolean isValid = validateReceipt(extractedText);
      String storeName = parseStoreName(extractedText);

      // 임시 파일 삭제
      deleteTempFile(tempFile);

      // 검증 결과 반환
      return new ReceiptScanResponseDto(
          isValid,
          storeName,
          isValid ? "영수증 검증에 성공했습니다." : "유효하지 않은 영수증입니다."
      );

    } catch (IOException e) {
      log.error("파일 처리 중 오류 발생", e);
      return new ReceiptScanResponseDto(false, null, "파일 처리 중 오류가 발생했습니다.");
    } catch (TesseractException e) {
      log.error("OCR 처리 중 오류 발생", e);
      return new ReceiptScanResponseDto(false, null, "OCR 처리 중 오류가 발생했습니다.");
    }
  }

  private void validateImage(MultipartFile image) {
    if (image == null || image.isEmpty()) {
      throw new IllegalArgumentException("업로드된 이미지가 비어있거나 존재하지 않습니다.");
    }

    String contentType = image.getContentType();
    if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
      throw new IllegalArgumentException("지원되지 않는 이미지 형식입니다. JPEG 또는 PNG 파일만 업로드 가능합니다.");
    }
  }

  private File convertToFile(MultipartFile file) throws IOException {
    Path tempPath = Files.createTempFile("receipt-", ".jpg");
    Files.copy(file.getInputStream(), tempPath, StandardCopyOption.REPLACE_EXISTING);
    File tempFile = tempPath.toFile();
    log.info("임시 파일 생성: {}", tempFile.getAbsolutePath());
    return tempFile;
  }

  protected String performOcr(File file) throws TesseractException {
    Tesseract tesseract = new Tesseract();
    tesseract.setDatapath(TESSDATA_PATH); // Tesseract 데이터 경로 설정
    tesseract.setLanguage(DEFAULT_LANGUAGE + "+" + SECONDARY_LANGUAGE); // 영어와 한국어 OCR 설정
    log.info("OCR 수행 시작: {}", file.getAbsolutePath());
    return tesseract.doOCR(file);
  }

  private boolean validateReceipt(String extractedText) {
    return extractedText.contains("Kids Cafe") && extractedText.contains("Total");
  }

  private String parseStoreName(String extractedText) {
    if (extractedText.contains("Store Name:")) {
      String[] parts = extractedText.split("Store Name:");
      if (parts.length > 1) {
        return parts[1].split("\n")[0].trim();
      }
    }
    return "알 수 없는 가게 이름";
  }

  private void deleteTempFile(File file) {
    try {
      if (file.exists() && !file.delete()) {
        log.warn("임시 파일 삭제 실패: {}", file.getAbsolutePath());
      } else {
        log.info("임시 파일 삭제 성공: {}", file.getAbsolutePath());
      }
    } catch (Exception e) {
      log.error("임시 파일 삭제 중 오류 발생", e);
    }
  }
}