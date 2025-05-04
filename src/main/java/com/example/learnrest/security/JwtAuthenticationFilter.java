package com.example.learnrest.security;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.example.learnrest.entity.User;
import com.example.learnrest.exception.JwtAuthenticationException;
import com.example.learnrest.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;
  private final HandlerExceptionResolver handlerExceptionResolver;

  public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository, HandlerExceptionResolver handlerExceptionResolver) {
    this.jwtUtil = jwtUtil;
    this.userRepository = userRepository;
    this.handlerExceptionResolver = handlerExceptionResolver;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getRequestURI();
    return path.startsWith("/api/v1/auth");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
  
      if (authHeader == null || !authHeader.startsWith("Bearer")) {
        throw new JwtAuthenticationException("Token is required");
      }
  
      String token = authHeader.substring(7);
      if(!jwtUtil.validateToken(token)) {
        throw new JwtAuthenticationException("Invalid or expired token");
      }
  
      String email = jwtUtil.extractSubject(token);
      User user = userRepository.findByEmail(email).orElseThrow(() -> 
        new JwtAuthenticationException("User not found")
      );
  
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        user, 
        null, 
        List.of() // Add a default role
      );
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      
      SecurityContextHolder.getContext().setAuthentication(authentication);
      filterChain.doFilter(request, response);
    } catch (Exception ex) {
      // Delegate to @ControllerAdvice
      handlerExceptionResolver.resolveException(request, response, null, ex);
    }
  }
}
