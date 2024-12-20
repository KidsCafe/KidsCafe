package com.sparta.kidscafe.domain.review.service;

import com.sparta.kidscafe.domain.review.dto.request.ReceiptScanRequestDto;
import com.sparta.kidscafe.domain.review.dto.response.ReceiptScanResponseDto;
import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReceiptScanServiceTest {

    private final ReceiptScanService receiptScanService = Mockito.spy(new ReceiptScanService());

    @Test
    @DisplayName("영수증 스캔 성공 - 유효한 영수증")
    void scanReceiptSuccess() throws IOException, TesseractException {
        // Given
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        Mockito.when(mockFile.getContentType()).thenReturn("image/jpeg");
        Mockito.when(mockFile.isEmpty()).thenReturn(false);

        // 테스트 리소스 파일 로드
        Path testReceiptPath = Path.of("./test_receipts/test_receipt_1.jpg");
        assert Files.exists(testReceiptPath) : "테스트 리소스 파일이 존재하지 않습니다.";
        Mockito.when(mockFile.getInputStream()).thenReturn(Files.newInputStream(testReceiptPath));

        ReceiptScanRequestDto requestDto = new ReceiptScanRequestDto();
        requestDto.setReceiptImage(mockFile);

        // OCR Mock 설정
        Mockito.doReturn("Store Name: Kids Cafe\nTotal: $10")
                .when(receiptScanService)
                .performOcr(Mockito.any(File.class));

        // When
        ReceiptScanResponseDto response = receiptScanService.scanReceipt(requestDto);

        // Then
        assertThat(response.isValid()).isTrue();
        assertThat(response.getStoreName()).isEqualTo("Kids Cafe");
        assertThat(response.getMessage()).isEqualTo("영수증 검증에 성공했습니다.");
    }

    @Test
    @DisplayName("영수증 스캔 실패 - 지원하지 않는 이미지 형식")
    void scanReceiptUnsupportedImageType() {
        // Given
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        Mockito.when(mockFile.getContentType()).thenReturn("image/gif");
        Mockito.when(mockFile.isEmpty()).thenReturn(false);

        ReceiptScanRequestDto requestDto = new ReceiptScanRequestDto();
        requestDto.setReceiptImage(mockFile);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> receiptScanService.scanReceipt(requestDto)
        );
        assertThat(exception.getMessage()).isEqualTo("지원되지 않는 이미지 형식입니다. JPEG 또는 PNG 파일만 업로드 가능합니다.");
    }

    @Test
    @DisplayName("영수증 스캔 실패 - 텍스트 검증 실패")
    void scanReceiptInvalidText() throws IOException, TesseractException {
        // Given
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        Mockito.when(mockFile.getContentType()).thenReturn("image/jpeg");
        Mockito.when(mockFile.isEmpty()).thenReturn(false);

        Path testReceiptPath = Path.of("./test_receipts/test_receipt_2.jpg");
        assert Files.exists(testReceiptPath) : "테스트 리소스 파일이 존재하지 않습니다.";
        Mockito.when(mockFile.getInputStream()).thenReturn(Files.newInputStream(testReceiptPath));

        ReceiptScanRequestDto requestDto = new ReceiptScanRequestDto();
        requestDto.setReceiptImage(mockFile);

        Mockito.doReturn("Random Text Without Receipt Info").when(receiptScanService).performOcr(Mockito.any(File.class));

        // When
        ReceiptScanResponseDto response = receiptScanService.scanReceipt(requestDto);

        // Then
        assertThat(response.isValid()).isFalse();
        assertThat(response.getStoreName()).isEqualTo("알 수 없는 가게 이름");
        assertThat(response.getMessage()).isEqualTo("유효하지 않은 영수증입니다.");
    }

    @Test
    @DisplayName("영수증 스캔 실패 - OCR 오류")
    void scanReceiptOcrError() throws IOException, TesseractException {
        // Given
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        Mockito.when(mockFile.getContentType()).thenReturn("image/jpeg");
        Mockito.when(mockFile.isEmpty()).thenReturn(false);

        Path testReceiptPath = Path.of("./test_receipts/test_receipt_3.jpg");
        assert Files.exists(testReceiptPath) : "테스트 리소스 파일이 존재하지 않습니다.";
        Mockito.when(mockFile.getInputStream()).thenReturn(Files.newInputStream(testReceiptPath));

        ReceiptScanRequestDto requestDto = new ReceiptScanRequestDto();
        requestDto.setReceiptImage(mockFile);

        Mockito.doThrow(new TesseractException("OCR 실패")).when(receiptScanService).performOcr(Mockito.any(File.class));

        // When
        ReceiptScanResponseDto response = receiptScanService.scanReceipt(requestDto);

        // Then
        assertThat(response.isValid()).isFalse();
        assertThat(response.getStoreName()).isNull();
        assertThat(response.getMessage()).isEqualTo("OCR 처리 중 오류가 발생했습니다.");
    }
}