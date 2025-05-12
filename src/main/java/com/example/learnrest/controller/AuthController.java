package com.example.learnrest.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learnrest.dto.JsonApiSingleResponse;
import com.example.learnrest.dto.request.auth.ForgotPasswordRequest;
import com.example.learnrest.dto.request.auth.LoginRequest;
import com.example.learnrest.dto.request.auth.RefreshTokenRequest;
import com.example.learnrest.dto.request.auth.RegisterRequest;
import com.example.learnrest.dto.request.auth.ResetPasswordRequest;
import com.example.learnrest.dto.request.auth.ValidateRequest;
import com.example.learnrest.service.UserService;
import com.example.learnrest.util.JsonApiHelper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<JsonApiSingleResponse> registerHandler(@Valid @RequestBody RegisterRequest req, HttpServletRequest request) {  
    userService.registerUser(req);
    
    return ResponseEntity.status(HttpStatus.CREATED).body(JsonApiHelper.createEmptySingleResponse("users", "-", "Success registering user"));
  }

  @PostMapping("/validate")
  public ResponseEntity<JsonApiSingleResponse> validateHandler(@Valid @RequestBody ValidateRequest req) {
    userService.validateEmail(req);
    
    return ResponseEntity.status(HttpStatus.OK).body(JsonApiHelper.createEmptySingleResponse("users", "-","Success validating user"));
  }

  @PostMapping("/login")
  public ResponseEntity<JsonApiSingleResponse> loginHandler(@Valid @RequestBody LoginRequest req,HttpServletRequest request ) {
    // Get IP address and user agent
    String ipAddress = request.getRemoteAddr();
    String userAgent = request.getHeader("User-Agent");

    Map<String, Object> responseData = userService.loginUser(req, ipAddress, userAgent);
    
    return ResponseEntity.status(HttpStatus.OK).body(JsonApiHelper.createSingleResponse("users", responseData, "Success login"));
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<JsonApiSingleResponse> forgotPasswordHandler(@Valid @RequestBody ForgotPasswordRequest req) {
    userService.generateResetPasswordToken(req);
    
    return ResponseEntity.status(HttpStatus.OK).body(JsonApiHelper.createEmptySingleResponse("user","-","Success send request reset password"));
  }  

  @PostMapping("/reset-password")
  public ResponseEntity<JsonApiSingleResponse> resetPasswordHandler(@Valid @RequestBody ResetPasswordRequest req) {
    userService.resetPassword(req);
    
    return ResponseEntity.status(HttpStatus.OK).body(JsonApiHelper.createEmptySingleResponse("user","-", "Success reset password"));   
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<JsonApiSingleResponse> refreshTokenHandler(@Valid @RequestBody RefreshTokenRequest req) {
    Map<String, Object> responseData = userService.refreshToken(req);

    return ResponseEntity.status(HttpStatus.OK).body(JsonApiHelper.createSingleResponse("user", responseData, "Success refresh token"));   
  }
  
}
