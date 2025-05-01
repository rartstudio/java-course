package com.example.learnrest.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.learnrest.dto.auth.LoginRequest;
import com.example.learnrest.dto.auth.RegisterRequest;
import com.example.learnrest.dto.auth.ValidateRequest;
import com.example.learnrest.entity.User;
import com.example.learnrest.exception.DuplicateEmailException;
import com.example.learnrest.exception.InvalidCredentialsException;
import com.example.learnrest.exception.ValidationTokenEmailExpiredException;
import com.example.learnrest.repository.UserRepository;
import com.example.learnrest.security.JwtUtil;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;

  public UserService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, EmailService emailService) {
    this.userRepository = userRepository;
    this.jwtUtil = jwtUtil;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
  }

  public Map<String, Object> registerUser(RegisterRequest req) {
    if (userRepository.existsByEmail(req.getEmail())) {
      throw new DuplicateEmailException("Email already registered");
    }

    // save to database
    User user = new User();
    user.setName(req.getName());
    user.setEmail(req.getEmail());
    user.setPassword(passwordEncoder.encode(req.getPassword()));

    // generate a unique validation token
    String validationToken = UUID.randomUUID().toString();
    LocalDateTime validationTokenExpiry = LocalDateTime.now().plusHours(24);

    user.setValidationToken(validationToken);
    user.setValidationTokenExpiry(validationTokenExpiry);
    userRepository.save(user);

    // send email
    emailService.sendValidationEmail(req.getEmail(), validationToken);

    // claims token
    Map<String, Object> claims = jwtUtil.generateClaims(req.getName());

    // token
    String token = jwtUtil.generateToken(req.getEmail(), claims);
    String refreshToken = jwtUtil.generateRefreshToken(req.getEmail());

    // result
    Map<String, Object> responseData = new HashMap<>();
    responseData.put("id", '-');
    responseData.put("access_token", token);
    responseData.put("refresh_token", refreshToken);

    return responseData;
  }

  public void validateEmail(ValidateRequest req) {
    // Find the user by their validation token
    User user = userRepository.findByValidationToken(req.getToken())
    .orElseThrow(() -> new ValidationTokenEmailExpiredException("Invalid validation token"));

    if (user.getValidationTokenExpiry().isBefore(LocalDateTime.now())) {
      throw new ValidationTokenEmailExpiredException("The validation token has expired");
    }

    // Mark the user's email as validated
    user.setValidationToken(null); // Clear the token
    user.setValidationTokenExpiry(null); // Clear the expiry

    LocalDateTime userValidateAt = LocalDateTime.now();
    user.setUserValidateAt(userValidateAt); // Mark email as verified
    userRepository.save(user);
  }

  public Map<String, Object> loginUser(LoginRequest req) {
    // Find the user by email
    User user = userRepository.findByEmail(req.getEmail())
    .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

    // Check if the password matches
    if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
      throw new InvalidCredentialsException("Invalid email or password");
    }

    // Generate JWT tokens
    Map<String, Object> claims = jwtUtil.generateClaims(user.getName());
    String accessToken = jwtUtil.generateToken(user.getEmail(), claims);
    String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

    // Build the response data
    Map<String, Object> responseData = new HashMap<>();
    responseData.put("id", user.getId());
    responseData.put("access_token", accessToken);
    responseData.put("refresh_token", refreshToken);

    return responseData;
  }
}
