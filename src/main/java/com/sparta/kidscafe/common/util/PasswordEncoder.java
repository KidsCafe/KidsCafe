package com.sparta.kidscafe.common.util;

import org.springframework.stereotype.Component;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Component
public class PasswordEncoder {
  /**
   * 단방향 암호화를 정적으로 수행합니다.
   *
   * @param rawPassword 암호화하고자 하는 문자열 형태의 비밀번호
   * @return BCrypt 로 단방향 해시 암호화한 문자열
   */
  public String encode(String rawPassword) {
    return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
  }

  /**
   * 입력 비밀번호와 실제 비밀번호의 해시를 통해 일치 여부를 비교하여 반환합니다.
   *
   * @param rawPassword     대조하고자 하는 입력 비밀번호
   * @param encodedPassword 실제 비밀번호의 해시된 문자열
   * @return 입력 비밀번호와 실제 비밀번호의 일치 여부
   */
  public boolean matches(String rawPassword, String encodedPassword) {
    BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword.toCharArray());
    return result.verified;
  }
}