package com.example.learnrest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.learnrest.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private final JwtAuthenticationFilter jwtAuthFilter;

  public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
    this.jwtAuthFilter = jwtAuthFilter;
  }
  
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable()) // Disable CSRF using Lambda DSL
        .authorizeHttpRequests(auth -> auth
        // .requestMatchers("/api/v1/users/**").authenticated()
        .anyRequest().authenticated()
        ).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}

