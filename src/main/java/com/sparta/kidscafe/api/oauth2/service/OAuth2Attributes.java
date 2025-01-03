package com.sparta.kidscafe.api.oauth2.service;

import com.sparta.kidscafe.api.oauth2.controller.dto.OAuth2UserProfileDto;
import com.sparta.kidscafe.common.enums.LoginType;
import java.util.Arrays;
import java.util.Map;

public enum OAuth2Attributes {
  GITHUB("github") {
    @Override
    public OAuth2UserProfileDto of(Map<String, Object> attributes) {
      return OAuth2UserProfileDto.builder()
          .oauthId(String.valueOf(attributes.get("id")))
          .email((String) attributes.get("email"))
          .name((String) attributes.get("name"))
          .nickname((String) attributes.get("name"))
          .address("[default_address]")
          .loginType(LoginType.OAUTH)
          .provider("GITHUB")
          .build();
    }
  },
  NAVER("naver") {
    @Override
    public OAuth2UserProfileDto of(Map<String, Object> attributes) {
      Map<String, Object> response = (Map<String, Object>) attributes.get("response");
      return OAuth2UserProfileDto.builder()
          .oauthId(String.valueOf(response.get("id")))
          .email((String) response.get("email"))
          .name((String) response.get("name"))
          .nickname((String) response.get("nickname"))
          .address("[default_address]")
          .loginType(LoginType.OAUTH)
          .provider("NAVER")
          .build();
    }
  },
  KAKAO("kakao") {
    @Override
    public OAuth2UserProfileDto of(Map<String, Object> attributes) {
      Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
      Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
      return OAuth2UserProfileDto.builder()
          .oauthId(String.valueOf(attributes.get("id")))
          .email((String) kakaoAccount.get("email"))
          .name((String) profile.get("nickname"))
          .nickname((String) profile.get("nickname"))
          .address("[default_address]")
          .loginType(LoginType.OAUTH)
          .provider("KAKAO")
          .build();
    }
  },
  GOOGLE("google"){
    @Override
    public OAuth2UserProfileDto of(Map<String, Object> attributes) {
      return OAuth2UserProfileDto.builder()
          .oauthId((String) attributes.get("sub"))
          .email((String) attributes.get("email"))
          .name((String) attributes.get("name"))
          .nickname((String) attributes.getOrDefault("given_name", "[default_nickname]"))
          .address("[default_address]")
          .loginType(LoginType.OAUTH)
          .provider("GOOGLE")
          .build();
    }
  };

  private final String providerName;

  OAuth2Attributes(String name) {
    this.providerName = name;
  }

  public static OAuth2UserProfileDto extract(String providerName, Map<String, Object> attributes) {
    return Arrays.stream(values())
        .filter(provider -> providerName.equals(provider.providerName))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 OAuth2 제공자입니다: " + providerName))
        .of(attributes);
  }

  public abstract OAuth2UserProfileDto of(Map<String, Object> attributes);
}
