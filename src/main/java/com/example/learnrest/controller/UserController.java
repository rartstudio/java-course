package com.example.learnrest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learnrest.entity.User;
import com.example.learnrest.repository.UserRepository;



@RestController
@RequestMapping("/api/v1/users")
public class UserController {
  private final UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping
  public List<User> getMethodName() {
    return userRepository.findAll();
  }
  
  @PostMapping
  public User postMethodName(@RequestBody User user) {
    return userRepository.save(user);
  }
}
