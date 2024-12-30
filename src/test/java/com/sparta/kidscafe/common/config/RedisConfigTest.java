package com.sparta.kidscafe.common.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class RedisConfigTest {

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  @Test
  public void testRedisConnection() {
    redisTemplate.opsForValue().set("springTestKey", "springTestValue");

    Object value = redisTemplate.opsForValue().get("springTestKey");

    assertNotNull(value);
    assertEquals("springTestValue", value.toString());
  }

}