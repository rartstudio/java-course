package com.example.learnrest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmailValidationUser(String to, String validationToken) {
        logger.info("Sending email asynchronously on thread: {}", Thread.currentThread().getName());

        String validationLink = "http://localhost:8080/api/v1/auth/validate?token=" + validationToken;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@example.com");
        message.setTo(to);
        message.setSubject("Verify Your Email Address");
        message.setText("Please click the link below to validate your email address:\n\n" + validationLink);
        mailSender.send(message);

        logger.info("Email sent successfully to: {}", to);
    }

    @Async
    public void sendEmailResetPassword(String to, String validationToken) {
        logger.info("Sending email asynchronously on thread: {}", Thread.currentThread().getName());

        String validationLink = "http://localhost:8080/api/v1/auth/validate?token=" + validationToken;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@example.com");
        message.setTo(to);
        message.setSubject("Verify Your Email Address");
        message.setText("Please click the link below to reset your password:\n\n" + validationLink);
        mailSender.send(message);

        logger.info("Email sent successfully to: {}", to);
    }
}
