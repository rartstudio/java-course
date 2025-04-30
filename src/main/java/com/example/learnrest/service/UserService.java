package com.example.learnrest.service;

import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.learnrest.dto.RegisterRequest;
import com.example.learnrest.entity.User;
import com.example.learnrest.exception.DuplicateEmailException;
import com.example.learnrest.repository.UserRepository;
import com.example.learnrest.security.JwtUtil;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.jwtUtil = jwtUtil;
    this.passwordEncoder = passwordEncoder;
  }

  public String registerUser(RegisterRequest req) {
    if (userRepository.existsByEmail(req.getEmail())) {
      throw new DuplicateEmailException("Email already registered");
    }

     // save to database
    User user = new User();
    user.setName(req.getName());
    user.setEmail(req.getEmail());
    user.setPassword(passwordEncoder.encode(req.getPassword()));
    userRepository.save(user);

    // generate token
    Map<String, Object> claims = jwtUtil.generateClaims(req.getName());

    return jwtUtil.generateToken(req.getEmail(), claims);
  }
}
