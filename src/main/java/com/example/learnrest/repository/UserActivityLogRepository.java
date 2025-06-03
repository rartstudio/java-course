package com.example.learnrest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.learnrest.entity.UserActivityLog;

public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long> {

}
