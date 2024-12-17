package com.sparta.kidscafe.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.user.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // 특정 RoleType에 해당하는 사용자 목록을 페이징 처리하여 반환
    List<User> findAllByRole(RoleType role);
    Page<User> findAllByRole(RoleType role, Pageable pageable);
    // 이메일 중복 여부 확인
    boolean existsByEmail(String email);
    // 이메일로 사용자 정보 조회
    Optional<User> findByEmail(String email);

  Optional<User> findByEmail(String email);

  Optional<User> findByOauthId(String id);

  Optional<User> findByOauthIdAndProvider(String oauthId, String provider);
    // 사장님의 가게를 즐겨찾기한 사용자 목록을 페이징 처리하여 반환
    Page<User> findUsersByBookmarks_UserId(Long userId, Pageable pageable);
}
