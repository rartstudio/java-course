package com.example.learnrest.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learnrest.dto.RegisterRequest;
import com.example.learnrest.security.JwtUtil;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final JwtUtil jwtUtil;

  public AuthController(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/register")
  public String registerHandler(@Valid @RequestBody RegisterRequest req) {
    Map<String, Object> claims = jwtUtil.generateClaims(req.getName());
    String token = jwtUtil.generateToken(req.getEmail(), claims);
      
    return token;
  }
}
