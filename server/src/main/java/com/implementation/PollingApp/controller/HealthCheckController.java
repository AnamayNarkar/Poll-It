package com.implementation.PollingApp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.implementation.PollingApp.util.ApiResponse;

@RestController
public class HealthCheckController {

    @GetMapping("/")
    public ResponseEntity<ApiResponse<Object>> healthCheckRequest() {
        ApiResponse<Object> response = new ApiResponse<>(null, "Journal App is up and running");
        return ResponseEntity.ok(response);
    }

}