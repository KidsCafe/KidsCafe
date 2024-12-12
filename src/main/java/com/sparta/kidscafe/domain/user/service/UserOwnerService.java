package com.sparta.kidscafe.domain.user.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ListResponseDto;
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
public class UserOwnerService {

    private final UserRepository userRepository;

    public ListResponseDto<UserResponseDto> getUsersWhoFavoritedOwner(AuthUser authUser, int page, int size) {
        // authUser에서 ownerId 가져오기
        Long ownerId = authUser.getId();

        // PageRequest 생성 (0-based index 사용)
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        // 사장님의 가게를 즐겨찾기한 사용자 조회 및 페이징 처리
        Page<User> userPage = userRepository.findUsersByBookmarks_UserId(ownerId, pageRequest);

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
