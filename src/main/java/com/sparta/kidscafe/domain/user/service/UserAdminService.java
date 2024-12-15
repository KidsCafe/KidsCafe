package com.sparta.kidscafe.domain.user.service;

import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.user.dto.response.UserResponseDto;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAdminService {

    private final UserRepository userRepository;

    public ListResponseDto<UserResponseDto> getAdminUsers(int page, int size) {
        // 입력값 유효성 검증
        if (page < 1 || size < 1) {
            throw new IllegalArgumentException("페이지 번호와 페이지 크기는 1 이상이어야 합니다.");
        }

        // PageRequest 생성 (0-based index 사용)
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        // RoleType.USER 필터링된 사용자 조회 및 페이징 처리
        Page<User> userPage = userRepository.findAllByRole(RoleType.USER, pageRequest);

        // 사용자 목록을 DTO로 변환
        List<UserResponseDto> users = userPage.getContent().stream()
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());

        // ListResponseDto로 감싸서 반환
        return ListResponseDto.success(
                users,
                HttpStatus.OK,
                "회원 조회 성공"
        );
    }
}