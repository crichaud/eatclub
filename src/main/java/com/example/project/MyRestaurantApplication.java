package com.example.project; // Root package

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyRestaurantApplication { // This is your "Application class name"
    public static void main(String[] args) {
        SpringApplication.run(MyRestaurantApplication.class, args);
    }
}
