package com.example.learnrest.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.learnrest.dto.request.auth.LoginRequest;
import com.example.learnrest.entity.User;
import com.example.learnrest.entity.UserSession;
import com.example.learnrest.exception.InvalidCredentialsException;
import com.example.learnrest.repository.UserRepository;
import com.example.learnrest.repository.UserSessionRepository;
import com.example.learnrest.security.JwtUtil;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmailService emailService;
    @Mock
    private UserSessionRepository userSessionRepository;
    @Mock
    private RedisTokenService redisTokenService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginUser_ShouldReturnToken_WhenCredentialsValid() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        String encodedPassword = "hashed_password";
        String ipAddress = "127.0.0.1";
        String userAgent = "Chrome";
        String accessToken = "access-token";
        String refreshToken = "refresh-token";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setName("Test User");
        user.setUserValidateAt(LocalDateTime.now());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtUtil.generateClaims(user.getName())).thenReturn(Map.of("name", user.getName()));
        when(jwtUtil.generateToken(eq(email), anyMap())).thenReturn(accessToken);
        when(userSessionRepository.findFirstByUserAndDeviceInfoAndRevokedFalseOrderByLoggedInAtDesc(eq(user),
                eq(userAgent)))
                .thenReturn(Optional.empty());
        when(jwtUtil.expiredDateAccessToken()).thenReturn(new Date(System.currentTimeMillis() + 60_000));
        when(jwtUtil.generateRefreshToken(email)).thenReturn(refreshToken);
        when(jwtUtil.expiredDateRefreshToken())
                .thenReturn(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000));

        // Act
        Map<String, Object> result = userService.loginUser(loginRequest, ipAddress, userAgent);

        // Assert
        assertEquals(user.getId(), result.get("id"));
        assertEquals(accessToken, result.get("accessToken"));
        assertEquals(refreshToken, result.get("refreshToken"));

        verify(redisTokenService).storeAccessToken(eq(email), eq(accessToken), anyLong());
        verify(userSessionRepository).save(any(UserSession.class));
    }

    @Test
    void loginUser_ShouldThrow_WhenPasswordInvalid() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrong");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("correct_hash");
        user.setUserValidateAt(LocalDateTime.now());

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "correct_hash")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> userService.loginUser(loginRequest, "ip", "agent"));
    }

    @Test
    void loginUser_ShouldThrow_WhenEmailNotFound() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("unknown@example.com");

        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class,
                () -> userService.loginUser(loginRequest, "ip", "agent"));
    }

    @Test
    void loginUser_ShouldThrow_WhenUserNotValidated() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        User user = new User();
        user.setEmail(email);
        user.setPassword("hashed_password");
        user.setUserValidateAt(null); // Not validated

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, "hashed_password")).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class,
                () -> userService.loginUser(loginRequest, "127.0.0.1", "Chrome"));
    }
}
