package com.example.learnrest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.learnrest.entity.User;
import com.example.learnrest.entity.UserSession;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
  List<UserSession> findByUser(User user);
  Optional<UserSession> findFirstByUserAndDeviceInfoAndRevokedFalseOrderByLoggedInAtDesc(User user, String userAgent);
  Optional<UserSession> findByUserAndRefreshTokenAndDeviceInfoAndRevokedFalse(User user, String refreshToken, String deviceInfo);
}
