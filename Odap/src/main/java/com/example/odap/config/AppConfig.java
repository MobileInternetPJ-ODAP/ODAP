package com.example.odap.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${myapp.upload-dir}")
    private String uploadDir;

    @Bean
    public String uploadDir() {
        return uploadDir;
    }
}
