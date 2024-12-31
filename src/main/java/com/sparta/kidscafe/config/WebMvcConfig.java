package com.sparta.kidscafe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**") // 모든 경로 허용
        .allowedOrigins("http://localhost:3000", "http://localhost:8080") // 허용할 출처
        .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드
        .allowedHeaders("*") // 모든 헤더 허용
        .allowCredentials(true); // 인증 정보 허용
  }
}
