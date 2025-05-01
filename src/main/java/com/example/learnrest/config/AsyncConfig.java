package com.example.learnrest.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync // Enable asynchronous processing
public class AsyncConfig {

  @Bean(name = "taskExecutor")
  public Executor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5); // Number of threads to keep in the pool
    executor.setMaxPoolSize(10); // Maximum number of threads in the pool
    executor.setQueueCapacity(100); // Queue size for pending tasks
    executor.setThreadNamePrefix("AsyncThread-"); // Prefix for thread names
    executor.initialize();
    return executor;
  }
}