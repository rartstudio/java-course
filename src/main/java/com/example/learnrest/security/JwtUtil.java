package com.example.learnrest.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
  private final SecretKey key;

  public JwtUtil(String secretKey) {
    this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(String subject, Map<String, Object> extraClaims) {
    return Jwts.builder()
      .subject(subject)
      .claims(extraClaims)
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
      .signWith(key)
      .compact();
  }

  public Map<String, Object> generateClaims(String name) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("name", name);

    return claims;
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
