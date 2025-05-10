package com.example.learnrest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.learnrest.entity.UserSession;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
  
}
