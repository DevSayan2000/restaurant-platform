package com.example.restaurantplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RestaurantPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantPlatformApplication.class, args);
    }
}