package com.implementation.PollingApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:5173") // Allow only your frontend's origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowCredentials(true) // Allow cookies
                .maxAge(3600);
    }
}
