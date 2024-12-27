package com.sparta.kidscafe.domain.user.dummy;

import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import com.sparta.kidscafe.dummy.DummyUser;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Tag("dummy-test")
public class UserDummyTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  @Rollback(false)
  void createUser() {
    userRepository.saveAll(DummyUser.createDummyUsers(RoleType.USER, 10));
  }

  @Test
  @Rollback(false)
  void createAdminUser() {
    userRepository.saveAll(DummyUser.createDummyUsers(RoleType.ADMIN, 10));
  }

  @Test
  @Rollback(false)
  void createOwnerUser() {
    userRepository.saveAll(DummyUser.createDummyUsers(RoleType.OWNER, 10));
  }
}
