package com.example.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    /**
     * Defines a singleton RestTemplate bean.
     * This instance will be shared across the application.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
