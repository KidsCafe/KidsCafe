// package com.sparta.kidscafe.common.config;
//
// import org.springframework.stereotype.Component;
//
// import at.favre.lib.crypto.bcrypt.BCrypt;
//
// @Component
// public class PasswordConfig {
//
//   public String encode(String password){
//     return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, password.toCharArray());
//   }
//
//   public boolean matches(String password, String encodedPassword){
//     BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), encodedPassword);
//     return result.verified;
//   }
// }
