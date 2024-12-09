package com.sparta.kidscafe.domain.user.repository;

import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.user.entity.User;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  List<User> findAllByRole(RoleType role);

  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);
}
