package com.example.learnrest.service;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisTokenService {
  private final RedisTemplate<String, String> redisTemplate;
  private static final Logger logger = LoggerFactory.getLogger(RedisTokenService.class);
  
  public RedisTokenService(RedisTemplate<String, String> redisTemplate) {
      this.redisTemplate = redisTemplate;
  }

  public void storeAccessToken(String email, String token, long ttlInSeconds) {
    logger.info("📆 TTL in seconds: " + ttlInSeconds);
    redisTemplate.opsForValue().set(email, token, ttlInSeconds, TimeUnit.SECONDS);
    logger.info("Saving token to Redis with key: " + token);
  }

  public String getAccessToken(String email) {
    String token = redisTemplate.opsForValue().get(email);
    logger.info("🔍 Retrieved token from Redis for key '{}': {}", email, token);
    return token;
  }
}
