package com.example.learnrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LearnrestApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnrestApplication.class, args);
    }

}
