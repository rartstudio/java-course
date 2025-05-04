package com.example.learnrest.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learnrest.dto.JsonApiResponse;
import com.example.learnrest.entity.User;
import com.example.learnrest.repository.UserRepository;
import com.example.learnrest.util.JsonApiHelper;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
  private final UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping("/profile")
  public ResponseEntity<JsonApiResponse> getProfile(@AuthenticationPrincipal User user) {
    // Fetch user from the database to ensure
    User dbUser = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

    // Use a mutable map instead of Map.of
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("id", dbUser.getId());
    attributes.put("name", dbUser.getName());
    attributes.put("email", dbUser.getEmail());

    return ResponseEntity.status(HttpStatus.OK).body(JsonApiHelper.createResponse("users", attributes));
  }
}
