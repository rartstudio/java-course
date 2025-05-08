package com.example.learnrest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.learnrest.security.JwtUtil;
import com.example.learnrest.service.S3Service;

@Configuration
public class AppConfig {

  @Value("${jwt.secret-key}")
  private String jwtSecretKey;

  @Value("${aws.s3.region}")
  private String s3Region;

  @Value("${aws.s3.access-key}")
  private String s3AccessKey;

  @Value("${aws.s3.secret-key}")
  private String s3SecretKey;

  @Value("${aws.s3.bucket-name}")
  private String s3BucketName;

  @Value("${aws.s3.endpoint}")
  private String s3Endpoint;

  @Bean
  public JwtUtil jwtUtil() {
    return new JwtUtil(jwtSecretKey);
  }

  @Bean
  public S3Service s3Service() {
    return new S3Service(s3Region, s3Endpoint, s3AccessKey, s3SecretKey, s3BucketName);
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
