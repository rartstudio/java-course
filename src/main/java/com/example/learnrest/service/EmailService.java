package com.example.learnrest.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
  private final JavaMailSender mailSender;
  
  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void sendValidationEmail(String to, String validationToken) {
    String validationLink = "http://localhost:8080/api/v1/auth/validate?token=" + validationToken;
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("no-reply@example.com");
    message.setTo(to);
    message.setSubject("Verify Your Email Address");
    message.setText("Please click the link below to validate your email address:\n\n" + validationLink);
    mailSender.send(message);
  }
}
