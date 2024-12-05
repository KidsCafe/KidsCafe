package com.sparta.kidscafe.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialLoginType {
  DEFAULT("default"),
  KAKAO("kakao"),
  NAVER("naver"),
  GOOGLE("google");

  private final String name;

  public static SocialLoginType getSocialLoginType(String name) {
    for (SocialLoginType socialType : SocialLoginType.values()) {
      if (socialType.getName().equals(name)) {
        return socialType;
      }
    }
    return DEFAULT;
  }
}
