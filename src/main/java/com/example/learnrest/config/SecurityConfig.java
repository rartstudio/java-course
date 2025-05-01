package com.example.learnrest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable()) // Disable CSRF using Lambda DSL
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/v1/auth/register").permitAll() // Allow public access to /register
            .requestMatchers("/api/v1/auth/validate").permitAll()
            .anyRequest().authenticated() // Protect all other endpoints
        );
    return http.build();
  }
}

