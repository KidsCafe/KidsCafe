package com.sparta.kidscafe.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.sparta.kidscafe.common.util.CustomPasswordEncoder;

// Spring Security에서 사용되는 비밀번호 암호화 설정 클래스
@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }
}
