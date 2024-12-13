package com.sparta.kidscafe.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // User 관련 에러
    USER_NOT_FOUND("USER_NOT_FOUND", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_EMAIL("DUPLICATE_EMAIL", "이미 사용 중인 이메일입니다.", HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD("WRONG_PASSWORD", "비밀번호가 틀립니다.", HttpStatus.BAD_REQUEST),

    // Cafe 관련 에러
    CAFE_NOT_FOUND("CAFE_NOT_FOUND", "카페를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_CAFE_DATA("INVALID_CAFE_DATA", "카페 데이터가 유효하지 않습니다.", HttpStatus.BAD_REQUEST),

    // Image 관련 에러
    IMAGE_UPLOAD_FAILED("IMAGE_UPLOAD_FAILED", "카페 이미지 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    IMAGE_REMOVE_FAILED("IMAGE_REMOVE_FAILED", "카페 이미지 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    IMAGE_NOT_EXIST("IMAGE_NOT_EXIST", "이미지가 존재하지 않습니다.",HttpStatus.INTERNAL_SERVER_ERROR),

    // Review 관련 에러
    REVIEW_NOT_FOUND("REVIEW_NOT_FOUND", "리뷰를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // Room 관련에러
    ROOM_NOT_FOUND("ROOM_NOT_FOUND","룸을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // Reservation 관련 에러
    RESERVATION_NOT_FOUND("RESERVATION_NOT_FOUND", "예약을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_STATUS_CHANGE("INVALID_STATUS_CHANGE", "유효하지 않은 상태입니다.", HttpStatus.BAD_REQUEST),
    // Bookmark 관련 에러
    NO_BOOKMARKS_FOUND("NO_BOOKMARKS_FOUND", "즐겨찾기 목록이 비어있습니다.", HttpStatus.NOT_FOUND),

    // PricePolicy 관련 에러
    PRICE_POLICY_NOT_FOUND("PRICE_POLICY_NOT_FOUND", "가격 정책을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PRICE_POLICY_MISMATCH("PRICE_POLICY_MISMATCH", "카페에 해당하지 않는 가격 정책입니다.", HttpStatus.BAD_REQUEST),

    // FEE 관련 에러
    FEE_TABLE_UNAUTHORIZED("FEE_TABLE_UNAUTHORIZED", "일반유저는 가격책정의 권한이 없습니다.", HttpStatus.FORBIDDEN),
    FEE_TABLE_OWN_CREATE("FEE_TABLE_OWN_CREATE", "사장님 본인 가게에만 가격책정이 가능합니다.", HttpStatus.FORBIDDEN),
    FEE_NOT_FOUND("FEE_NOT_FOUND", "해당 요금을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // Fee 관련 에러
    INVALID_FEE_ID("INVALID_FEE_ID", "유효하지 않은 입장료입니다.", HttpStatus.BAD_REQUEST),
    UNSUPPORTED_TARGET_TYPE("UNSUPPORTED_TARGET_TYPE", "지원하지 않는 유형입니다.", HttpStatus.BAD_REQUEST),

    // 신고 관련 에러
    REPORT_NOT_FOUND("REPORT_NOT_FOUND","신고를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_REPORT_STATUS("INVALID_REPORT_STATUS", "잘못된 신고 상태입니다.", HttpStatus.BAD_REQUEST),
    REPORT_STATUS_NOT_CHANGEABLE("REPORT_STATUS_NOT_CHANGEABLE", "현재 상태에서 요청된 상태로 변경할 수 없습니다.", HttpStatus.BAD_REQUEST),

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
