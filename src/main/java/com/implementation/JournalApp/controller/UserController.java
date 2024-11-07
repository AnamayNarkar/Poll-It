package com.implementation.JournalApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.implementation.JournalApp.dto.UserDto;
import com.implementation.JournalApp.entity.UserEntity;
import com.implementation.JournalApp.service.UserService;

import org.springframework.http.ResponseEntity;
import com.implementation.JournalApp.util.ApiResponse;

@Controller
@RestController
@RequestMapping("/api/user/")
public class UserController {

        @Autowired
        private UserService userService;

        @PostMapping("/register")
        public ResponseEntity<ApiResponse<UserEntity>> registerUser(@RequestBody UserDto userDto) {
                UserEntity userEntity = userService.createUser(userDto);
                ApiResponse<UserEntity> response = new ApiResponse<>(userEntity, "User created successfully");
                return ResponseEntity.ok(response);
        }

        @PostMapping("/login")
        public ResponseEntity<ApiResponse<UserEntity>> loginUser(@RequestBody UserDto userDto) {
                UserEntity userEntity = userService.loginUser(userDto);
                ApiResponse<UserEntity> response = new ApiResponse<>(userEntity, "User logged in successfully");
                return ResponseEntity.ok(response);
        }

}