package com.example.learnrest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learnrest.dto.JsonApiListResponse;
import com.example.learnrest.dto.JsonApiSingleResponse;
import com.example.learnrest.dto.request.user.ChangePasswordRequest;
import com.example.learnrest.dto.request.user.CreateProfileForm;
import com.example.learnrest.dto.request.user.LogoutRequest;
import com.example.learnrest.entity.User;
import com.example.learnrest.entity.UserProfile;
import com.example.learnrest.entity.UserSession;
import com.example.learnrest.exception.DataConflictException;
import com.example.learnrest.service.UserProfileService;
import com.example.learnrest.service.UserService;
import com.example.learnrest.util.JsonApiHelper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserProfileService userProfileService;

    public UserController(UserService userService, UserProfileService userProfileService) {
        this.userService = userService;
        this.userProfileService = userProfileService;
    }

    @GetMapping("/profile")
    public ResponseEntity<JsonApiSingleResponse> getProfileHandler(@AuthenticationPrincipal User user) {
        // Fetch user from the database to ensure
        User dbUser = userService.getUserData(user.getId());

        UserProfile profile = dbUser.getProfile(); // might be null

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", dbUser.getId());
        attributes.put("name", dbUser.getName());
        attributes.put("email", dbUser.getEmail());
        attributes.put("image", profile != null ? profile.getImage() : null);
        attributes.put("dateOfBirth", profile != null ? profile.getDateOfBirth() : null);

        return ResponseEntity.status(HttpStatus.OK)
                .body(JsonApiHelper.createSingleResponse("users", attributes, "Success get user profile"));
    }

    @PostMapping(value = "/profile", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<JsonApiSingleResponse> createProfileHandler(@Valid @ModelAttribute CreateProfileForm form,
            @AuthenticationPrincipal User user) {
        User dbUser = userService.getUser(user.getEmail());

        Optional<UserProfile> userProfile = userProfileService.getProfile(dbUser);

        if (userProfile.isPresent()) {
            throw new DataConflictException("Data profile sudah ada");
        }

        UserProfile profile = userProfileService.createProfile(form, user);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("image", profile.getImage());
        attributes.put("dateOfBirth", profile.getDateOfBirth());

        return ResponseEntity.status(HttpStatus.OK)
                .body(JsonApiHelper.createSingleResponse("users", attributes, "Success create user profile"));
    }

    @PostMapping("/change-password")
    public ResponseEntity<JsonApiSingleResponse> changePasswordHandler(@Valid @RequestBody ChangePasswordRequest req,
            @AuthenticationPrincipal User user) {
        User dbUser = userService.getUser(user.getEmail());

        userService.changePassword(req, dbUser);

        return ResponseEntity.status(HttpStatus.OK)
                .body(JsonApiHelper.createEmptySingleResponse("users", "-", "Success change password"));
    }

    @PostMapping("/logout")
    public ResponseEntity<JsonApiSingleResponse> logoutHandler(@Valid @RequestBody LogoutRequest req,
            @AuthenticationPrincipal User user, HttpServletRequest request) {
        User dbUser = userService.getUser(user.getEmail());

        // Get IP address and user agent
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        userService.logoutUser(dbUser, req, ipAddress, userAgent);

        return ResponseEntity.status(HttpStatus.OK)
                .body(JsonApiHelper.createEmptySingleResponse("users", "-", "Success logout user"));
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

        return ResponseEntity.status(HttpStatus.OK)
                .body(JsonApiHelper.createListResponse("user_sessions", attributes, "Success get user sessions"));
    }

    @DeleteMapping(value = "/sessions/{id}")
    public ResponseEntity<JsonApiSingleResponse> deleteUserSessionHandler(@AuthenticationPrincipal User user,
            @PathVariable Long id) {
        User dbUser = userService.getUser(user.getEmail());
        userService.removeUserSession(dbUser, id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(JsonApiHelper.createEmptySingleResponse("user_sessions", "-", "Success remove session"));
    }
}
