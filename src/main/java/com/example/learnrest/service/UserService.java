package com.example.learnrest.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.learnrest.dto.request.auth.ForgotPasswordRequest;
import com.example.learnrest.dto.request.auth.LoginRequest;
import com.example.learnrest.dto.request.auth.RefreshTokenRequest;
import com.example.learnrest.dto.request.auth.RegisterRequest;
import com.example.learnrest.dto.request.auth.ResetPasswordRequest;
import com.example.learnrest.dto.request.auth.ValidateRequest;
import com.example.learnrest.dto.request.user.CreateProfileForm;
import com.example.learnrest.entity.User;
import com.example.learnrest.entity.UserProfile;
import com.example.learnrest.entity.UserSession;
import com.example.learnrest.exception.DuplicateEmailException;
import com.example.learnrest.exception.ImageUploadException;
import com.example.learnrest.exception.InvalidCredentialsException;
import com.example.learnrest.exception.InvalidRefreshTokenException;
import com.example.learnrest.exception.NotFoundException;
import com.example.learnrest.exception.ValidationTokenExpiredException;
import com.example.learnrest.repository.UserProfileRepository;
import com.example.learnrest.repository.UserRepository;
import com.example.learnrest.repository.UserSessionRepository;
import com.example.learnrest.security.JwtUtil;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;
  private final S3Service s3Service;
  private final UserProfileRepository userProfileRepository;
  private final UserSessionRepository userSessionRepository;
  private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

  public UserService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, EmailService emailService, S3Service s3Service, UserProfileRepository userProfileRepository, UserSessionRepository userSessionRepository) {
    this.userRepository = userRepository;
    this.jwtUtil = jwtUtil;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
    this.s3Service = s3Service;
    this.userProfileRepository = userProfileRepository;
    this.userSessionRepository = userSessionRepository;
  }

  public Map<String, Object> registerUser(RegisterRequest req, String ipAddress, String userAgent) {
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
    emailService.sendEmailValidationUser(req.getEmail(), validationToken);

    // claims token
    Map<String, Object> claims = jwtUtil.generateClaims(req.getName());

    // token
    String token = jwtUtil.generateToken(req.getEmail(), claims);
    String refreshToken = jwtUtil.generateRefreshToken(req.getEmail());

    // save session
    UserSession session = new UserSession();
    session.setUser(user);
    session.setRefreshToken(refreshToken);
    session.setIpAddress(ipAddress);
    session.setDeviceInfo(userAgent);
    session.setLoggedInAt(new Date());
    session.setExpiresAt(new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000)); // 7 days
    session.setRevoked(false);
    userSessionRepository.save(session);

    // result
    Map<String, Object> responseData = new HashMap<>();
    responseData.put("id", '-');
    responseData.put("accessToken", token);
    responseData.put("refreshToken", refreshToken);

    return responseData;
  }

  public void validateEmail(ValidateRequest req) {
    // Find the user by their validation token
    User user = userRepository.findByValidationToken(req.getToken())
    .orElseThrow(() -> new ValidationTokenExpiredException("Invalid validation token"));

    if (user.getValidationTokenExpiry().isBefore(LocalDateTime.now())) {
      throw new ValidationTokenExpiredException("The validation token has expired");
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

    // check if user is validated
    if (user.getUserValidateAt() == null) {
      throw new InvalidCredentialsException("Account not validated yet");
    }

    // Generate JWT tokens
    Map<String, Object> claims = jwtUtil.generateClaims(user.getName());
    String accessToken = jwtUtil.generateToken(user.getEmail(), claims);
    String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

    // Build the response data
    Map<String, Object> responseData = new HashMap<>();
    responseData.put("id", user.getId());
    responseData.put("accessToken", accessToken);
    responseData.put("refreshToken", refreshToken);

    return responseData;
  }

  public User getUser(String email) {
    User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));

    return user;
  }

  public UserProfile createProfile(CreateProfileForm form, String email) {
    MultipartFile image = form.getImage();

    String imageUrl;
    try {
      imageUrl = s3Service.uploadFile(image);
    } catch (Exception e) {
      throw new ImageUploadException("Failed to upload image to S3");
    }

    User user = getUser(email);

    UserProfile profile = new UserProfile();
    profile.setDateOfBirth(form.getDateOfBirth());
    profile.setImage(imageUrl);
    profile.setUser(user);

    userProfileRepository.save(profile);

    UserProfile saved = userProfileRepository.save(profile);

    return saved;
  }

  public void generateResetPasswordToken(ForgotPasswordRequest req) {
    Optional<User> userOpt = userRepository.findByEmail(req.getEmail());
    if (userOpt.isEmpty()) {
      // Don't leak email info — silently ignore
      return;
    }

    // get the user data
    User user = userOpt.get();

    // generate token and expired time token
    String token = UUID.randomUUID().toString();
    LocalDateTime expiry = LocalDateTime.now().plusMinutes(30);

    // save it to database
    user.setResetPasswordToken(token);
    user.setResetPasswordTokenExpiry(expiry);
    userRepository.save(user);

    // send email
    emailService.sendEmailValidationUser(req.getEmail(), token);
  }

  public void resetPassword(ResetPasswordRequest req) {
    User user = userRepository.findByResetPasswordToken(req.getToken())
    .orElseThrow(() -> new ValidationTokenExpiredException("Invalid validation token"));

    logger.info("Email sent successfully to: {}", user.getResetPasswordToken());

    if (user.getResetPasswordTokenExpiry() == null || user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
      throw new ValidationTokenExpiredException("Reset token has expired");
    }

    user.setPassword(passwordEncoder.encode(req.getPassword()));
    user.setResetPasswordToken(null);
    user.setResetPasswordTokenExpiry(null);

    userRepository.save(user);
  }

  public Map<String, Object> refreshToken(RefreshTokenRequest req) {
    if (!jwtUtil.validateToken(req.getRefreshToken())) {
      throw new InvalidRefreshTokenException("Invalid refresh token");
    }

    String email = jwtUtil.extractSubject(req.getRefreshToken());
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException("User not found"));

    Map<String, Object> claims = jwtUtil.generateClaims(user.getName());

    String newAccessToken = jwtUtil.generateToken(user.getEmail(), claims);
    String newRefreshToken = jwtUtil.generateRefreshToken(user.getEmail());

    Map<String, Object> tokens = new HashMap<>();
    tokens.put("accessToken", newAccessToken);
    tokens.put("refreshToken", newRefreshToken);

    return tokens;
  }

  public List<UserSession> getUserSession(User user) {
    List<UserSession> sessions = userSessionRepository.findByUser(user);

    return sessions;
  }
}
