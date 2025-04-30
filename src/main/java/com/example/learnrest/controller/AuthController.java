package com.example.learnrest.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learnrest.dto.RegisterRequest;
import com.example.learnrest.entity.User;
import com.example.learnrest.exception.DuplicateEmailException;
import com.example.learnrest.repository.UserRepository;
import com.example.learnrest.security.JwtUtil;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;

  public AuthController(JwtUtil jwtUtil, UserRepository userRepository) {
    this.jwtUtil = jwtUtil;
    this.userRepository = userRepository;
  }

  @PostMapping("/register")
  public String registerHandler(@Valid @RequestBody RegisterRequest req) {
    if (userRepository.existsByEmail(req.getEmail())) {
      throw new DuplicateEmailException("Email already exist");
    }

    // save to database
    User user = new User();
    user.setName(req.getName());
    user.setEmail(req.getEmail());
    user.setPassword(req.getPassword());
    userRepository.save(user);

    // generate token
    Map<String, Object> claims = jwtUtil.generateClaims(req.getName());
    String token = jwtUtil.generateToken(req.getEmail(), claims);
      
    return token;
  }
}
