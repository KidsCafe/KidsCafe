package com.sparta.kidscafe.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "exception:")
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 입력 관련 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> BaseException(MethodArgumentNotValidException ex, HttpServletRequest req) {
        // 에러 메시지 추출
        String errorMsg = ex.getBindingResult().
            getAllErrors()
            .get(0)
            .getDefaultMessage();

        return baseException(req, errorMsg);
    }

    /**
     * ResponseException 예외 처리
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> BaseException(BusinessException ex, HttpServletRequest req) {
        return baseException(req, ex);
    }

    /**
     * 그 외 기타 예외처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> BaseException(Exception ex, HttpServletRequest req) {
        printError(ex);
        return baseException(req, ErrorCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * 기본적인 예외 처리를 위한 메서드입니다.
     *
     * @param req          HTTP 요청 객체
     * @param errorCode 응답 코드
     * @return ResponseEntity 객체
     */
    private ResponseEntity<ErrorResponseDto> baseException(HttpServletRequest req, ErrorCode errorCode) {
        String url = req.getRequestURL().toString();
        return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(new ErrorResponseDto(errorCode, url));
    }

    /**
     * 유효성 검사 실패 시 발생하는 예외 처리를 위한 메서드입니다.
     *
     * @param req      HTTP 요청 객체
     * @param errorMsg 에러 메시지
     * @return ResponseEntity 객체
     */
    private ResponseEntity<ErrorResponseDto> baseException(HttpServletRequest req, String errorMsg) {
        String url = req.getRequestURL().toString();
        return ResponseEntity
            .status(ErrorCode.BAD_REQUEST.getHttpStatus())
            .body(new ErrorResponseDto(ErrorCode.BAD_REQUEST, url, errorMsg));
    }

    /**
     * 기본적인 예외 처리를 위한 메서드입니다.
     *
     * @param req HTTP 요청 객체
     * @param ex  발생한 예외 객체
     * @return HTTP 응답 객체
     */
    private ResponseEntity<ErrorResponseDto> baseException(HttpServletRequest req, BusinessException ex) {
        String url = req.getRequestURL().toString();
        return ResponseEntity
            .status(ex.getErrorCode().getHttpStatus())
            .body(new ErrorResponseDto(ex, url));
    }

    /**
     * 예외 객체의 스택 트레이스를 배열로 가져옵니다.
     *
     * @param ex 발생한 예외 객체
     */
    public void printError(Exception ex) {
        StackTraceElement[] stackTraceElements = ex.getStackTrace();
        log.error(ex.getMessage(), stackTraceElements[0].toString());
    }
}