package com.sparta.kidscafe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class KidscafeApplication {

    public static void main(String[] args) {
        SpringApplication.run(KidscafeApplication.class, args);
    }

}
