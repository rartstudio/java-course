package com.example.learnrest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learnrest.dto.JsonApiListResponse;
import com.example.learnrest.dto.JsonApiSingleResponse;
import com.example.learnrest.dto.request.user.CreateProfileForm;
import com.example.learnrest.entity.User;
import com.example.learnrest.entity.UserProfile;
import com.example.learnrest.entity.UserSession;
import com.example.learnrest.service.UserService;
import com.example.learnrest.util.JsonApiHelper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/profile")
  public ResponseEntity<JsonApiSingleResponse> getProfileHandler(@AuthenticationPrincipal User user) {
    // Fetch user from the database to ensure
    User dbUser = userService.getUser(user.getEmail());

    Map<String, Object> attributes = new HashMap<>();
    attributes.put("id", dbUser.getId());
    attributes.put("name", dbUser.getName());
    attributes.put("email", dbUser.getEmail());

    return ResponseEntity.status(HttpStatus.OK).body(JsonApiHelper.createSingleResponse("users", attributes, "Success get user profile"));
  }

  @PostMapping(value = "/profile", consumes={MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<JsonApiSingleResponse> createProfileHandler(@Valid @ModelAttribute CreateProfileForm form,  @AuthenticationPrincipal User user) {
    UserProfile profile = userService.createProfile(form, user.getEmail());

    Map<String, Object> attributes = new HashMap<>();
    attributes.put("image", profile.getImage());
    attributes.put("dateOfBirth", profile.getDateOfBirth());
      
    return ResponseEntity.status(HttpStatus.OK).body(JsonApiHelper.createSingleResponse("users", attributes, "Success create user profile"));
  }
  
  @GetMapping(value = "/sessions")
  public ResponseEntity<JsonApiListResponse> getUserSessionHandler(@AuthenticationPrincipal User user) {
    List<UserSession> sessions = userService.getUserSession(user);

    List<Map<String, Object>> attributes = sessions.stream()
        .map(session -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", session.getId());
            item.put("deviceInfo", session.getDeviceInfo());
            item.put("ipAddress", session.getIpAddress());
            item.put("loggedInAt", session.getLoggedInAt());
            item.put("revoked", session.isRevoked());
            return item;
        })
        .toList();

    return ResponseEntity.status(HttpStatus.OK).body(JsonApiHelper.createListResponse("users", attributes,"Success get user sessions"));
  } 
}
