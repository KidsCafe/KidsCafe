package com.sparta.kidscafe.domain.user.entity;

import com.sparta.kidscafe.common.enums.LoginType;
import com.sparta.kidscafe.common.enums.RoleType;

public interface OAuthMember {
  Long getId();

  String getEmail();

  String getAddress();

  LoginType getLoginType();

  String getOauthId();

  String getName();

  RoleType getRole();

  String getPassword();

}
