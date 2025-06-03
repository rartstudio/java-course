-- Migration: CreateTableUserActivityLogs
-- Created at: Tue Jun  3 21:47:47 +07 2025

-- Write your SQL commands here
CREATE TABLE user_activity_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(255),
    ip_address VARCHAR(45),
    device_info VARCHAR(255),
    activity_type VARCHAR(100),
    activity_time DATETIME,
    user_id BIGINT,
    CONSTRAINT fk_user_activity_logs_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);