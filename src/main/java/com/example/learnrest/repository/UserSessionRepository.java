package com.example.learnrest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.learnrest.entity.User;
import com.example.learnrest.entity.UserSession;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
  List<UserSession> findByUser(User user);
}
