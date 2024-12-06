package com.sparta.kidscafe.dummy;

import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.common.enums.SocialLoginType;
import com.sparta.kidscafe.common.util.PasswordEncoder;
import com.sparta.kidscafe.common.util.TestUtil;
import com.sparta.kidscafe.domain.user.entity.User;
import java.util.ArrayList;
import java.util.List;

public class DummyUser {

  public static User createDummyUser(RoleType roleType) {
    PasswordEncoder passwordEncoder = new PasswordEncoder();
    String randomName = TestUtil.getRandomString(10);
    String randomNickname = TestUtil.getRandomString(10);
    String randomEmail = randomNickname + "@email.com";
    String randomPassword = passwordEncoder.encode(TestUtil.getRandomString(10));
    return User.builder()
        .email(randomEmail)
        .password(randomPassword)
        .nickname(randomNickname)
        .name(randomName)
        .address("대한민국 어딘가")
        .role(roleType)
        .socialType(SocialLoginType.DEFAULT)
        .build();
  }

  public static List<User> createDummyUsers(RoleType roleType, int size) {
    List<User> users = new ArrayList<>();
    for (int idx = 0; idx < size; idx++)
      users.add(createDummyUser(roleType));
    return users;
  }
}
