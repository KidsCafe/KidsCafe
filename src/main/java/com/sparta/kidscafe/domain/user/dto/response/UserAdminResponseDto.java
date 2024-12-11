package com.sparta.kidscafe.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserAdminResponseDto {

    private int status; // HTTP 상태 코드
    private String message; // 응답 메시지
    private List<UserResponseDto> data; // 회원 정보 리스트
    private int page; // 현재 페이지
    private int totalPage; // 전체 페이지 수
    private int size; // 페이지 당 회원 수
}
