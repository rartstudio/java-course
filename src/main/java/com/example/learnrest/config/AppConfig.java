package com.example.learnrest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.learnrest.security.JwtUtil;

@Configuration
public class AppConfig {

  @Value("${jwt.secret-key}")
  private String secretKey;

  @Bean
  public JwtUtil jwtUtil() {
    return new JwtUtil(secretKey);
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
