package com.trackdsa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class TrackdsaApplication {
    public static void main(String[] args) {
        SpringApplication.run(TrackdsaApplication.class, args);
    }
} 