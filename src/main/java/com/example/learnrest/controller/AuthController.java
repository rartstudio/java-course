package com.example.learnrest.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learnrest.dto.JsonApiResponse;
import com.example.learnrest.dto.RegisterRequest;
import com.example.learnrest.service.UserService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public JsonApiResponse registerHandler(@Valid @RequestBody RegisterRequest req) {  
    return userService.registerUser(req);
  }
}
