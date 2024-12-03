package com.implementation.PollingApp.controller;

import com.implementation.PollingApp.dto.UserDto;
import com.implementation.PollingApp.entity.SessionValueEntity;
import com.implementation.PollingApp.entity.UserEntity;
import com.implementation.PollingApp.service.UserService;
import com.implementation.PollingApp.util.ApiResponse;
import com.implementation.PollingApp.util.SessionUtils;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RestController
@RequestMapping("/api/user/")
public class UserController {

        @Autowired
        private UserService userService;

        @Autowired
        public SessionUtils sessionUtils;

        @GetMapping("/verifyUser")
        public ResponseEntity<ApiResponse<String>> verifyUser(HttpServletRequest request) {
                SessionValueEntity sessionValueEntity = sessionUtils.getUserSession(request);
                if (sessionValueEntity == null) {
                        ApiResponse<String> apiResponse = new ApiResponse<>(null, "User verification failed");
                        return ResponseEntity.status(401).body(apiResponse);
                }
                ApiResponse<String> apiResponse = new ApiResponse<>(null, "User verified successfully");
                return ResponseEntity.ok(apiResponse);
        }

        @PostMapping("/register")
        public ResponseEntity<ApiResponse<UserEntity>> registerUser(@RequestBody UserDto userDto, HttpServletResponse response) {
                UserEntity userEntity = userService.createUser(userDto);
                sessionUtils.saveUserSession(response, userEntity);
                ApiResponse<UserEntity> apiResponse = new ApiResponse<>(userEntity, "User created successfully");
                return ResponseEntity.ok(apiResponse);
        }

        @PostMapping("/login")
        public ResponseEntity<ApiResponse<UserEntity>> loginUser(@RequestBody UserDto userDto, HttpServletResponse response) {
                UserEntity userEntity = userService.loginUser(userDto);
                sessionUtils.saveUserSession(response, userEntity);
                ApiResponse<UserEntity> apiresponse = new ApiResponse<>(userEntity, "User logged in successfully");
                return ResponseEntity.ok(apiresponse);
        }

        @PostMapping("/logout")
        public ResponseEntity<ApiResponse<String>> logoutUser(HttpServletResponse response, HttpServletRequest request) {
                sessionUtils.clearUserSessionAndRemoveCookie(response, request);
                ApiResponse<String> apiResponse = new ApiResponse<>(null, "User logged out successfully");
                return ResponseEntity.ok(apiResponse);
        }

}
