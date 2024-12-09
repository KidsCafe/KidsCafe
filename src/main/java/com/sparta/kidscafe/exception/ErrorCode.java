package com.sparta.kidscafe.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // User 관련 에러
    USER_NOT_FOUND("USER_NOT_FOUND", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_EMAIL("DUPLICATE_EMAIL", "이미 사용 중인 이메일입니다.", HttpStatus.BAD_REQUEST),

    // Cafe 관련 에러
    CAFE_NOT_FOUND("CAFE_NOT_FOUND", "카페를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_CAFE_DATA("INVALID_CAFE_DATA", "카페 데이터가 유효하지 않습니다.", HttpStatus.BAD_REQUEST),

    // Review 관련 에러
    REVIEW_NOT_FOUND("REVIEW_NOT_FOUND", "리뷰를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // Reservation 관련 에러
    RESERVATION_NOT_FOUND("RESERVATION_NOT_FOUND", "예약을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // === guguggagga ===
    CAFE_IMAGE_UPLOAD_FAILED("", "카페 이미지 업로드 실패", HttpStatus.INTERNAL_SERVER_ERROR),
    // ==================

    // 공통 에러
    BAD_REQUEST("BAD_REQUEST", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "서버 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED("UNAUTHORIZED", "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("FORBIDDEN", "권한이 없습니다.", HttpStatus.FORBIDDEN);

    // 필드 정의
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
