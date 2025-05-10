-- Migration: CreateTableUserSession
-- Created at: Sat May 10 15:37:21 +07 2025

-- Write your SQL commands here
CREATE TABLE user_session (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    refresh_token VARCHAR(512) NOT NULL,
    device_info VARCHAR(255),
    ip_address VARCHAR(45),
    logged_in_at DATETIME,
    expires_at DATETIME,
    revoked BOOLEAN DEFAULT FALSE,
    last_used_at DATETIME,
    user_id BIGINT,
    CONSTRAINT fk_user_session_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
