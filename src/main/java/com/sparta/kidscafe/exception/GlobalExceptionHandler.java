package com.sparta.kidscafe.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "GlobalExceptionHandler")
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Validation 실패 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest req) {

        // 에러 메시지 추출
        String errorMsg = extractErrorMessage(ex.getBindingResult());
        log.warn("Validation Error: {}", errorMsg);

        return buildErrorResponse(req, ErrorCode.BAD_REQUEST, errorMsg);
    }

    // 비즈니스 로직 예외 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessException(
            BusinessException ex, HttpServletRequest req) {

        log.warn("Business Exception: {}", ex.getMessage());
        return buildErrorResponse(req, ex.getErrorCode(), ex.getMessage());
    }

    // 기타 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(
            Exception ex, HttpServletRequest req) {

        log.error("Unhandled Exception: {}", ex.getMessage(), ex);
        return buildErrorResponse(req, ErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    // Helper Method: 에러 메시지 추출
    private String extractErrorMessage(BindingResult bindingResult) {
        return bindingResult.getAllErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Validation failed.");
    }

    // Helper Method: ErrorResponseDto 생성
    private ResponseEntity<ErrorResponseDto> buildErrorResponse(
            HttpServletRequest req, ErrorCode errorCode, String customMessage) {

        String url = req.getRequestURL().toString();
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(errorCode, url, customMessage);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponseDto);
    }
}