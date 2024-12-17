package com.sparta.kidscafe.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
  List<User> findAllByRole(RoleType role);

  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);

  Optional<User> findByOauthId(String id);

  Optional<User> findByOauthIdAndProvider(String oauthId, String provider);
}
