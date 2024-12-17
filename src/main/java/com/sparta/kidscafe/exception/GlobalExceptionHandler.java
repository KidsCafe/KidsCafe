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
  public ResponseEntity<ErrorResponseDto> handleValidationException(
      MethodArgumentNotValidException ex,
      HttpServletRequest req) {
    printError(ex);
    return baseValidationException(req, extractErrorMessage(ex));
  }

  /**
   * ResponseException 예외 처리
   */
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponseDto> handleBusinessException(
      BusinessException ex,
      HttpServletRequest req) {
    printError(ex);
    return baseBusinessException(req, ex);
  }

  /**
   * 그 외 기타 예외처리
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDto> handleGenericException(
      Exception ex,
      HttpServletRequest req) {
    printError(ex);
    return baseGenericException(req);
  }

  /**
   * 기본적인 예외 처리를 위한 메서드입니다.
   *
   * @param req HTTP 요청 객체
   * @return ResponseEntity 객체
   */
  private ResponseEntity<ErrorResponseDto> baseGenericException(
      HttpServletRequest req) {
    ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
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
  private ResponseEntity<ErrorResponseDto> baseValidationException(
      HttpServletRequest req,
      String errorMsg) {
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
  private ResponseEntity<ErrorResponseDto> baseBusinessException(
      HttpServletRequest req,
      BusinessException ex) {
    String url = req.getRequestURL().toString();
    return ResponseEntity
        .status(ex.getErrorCode().getHttpStatus())
        .body(new ErrorResponseDto(ex, url));
  }

  /**
   * @param ex 발생한 예외 객체
   * @valid 에러 메세지를 같이 반환한다.
   */
  private String extractErrorMessage(MethodArgumentNotValidException ex) {
    return ex.getBindingResult().
        getAllErrors()
        .get(0)
        .getDefaultMessage();
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