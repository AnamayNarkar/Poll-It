package com.implementation.PollingApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.implementation.PollingApp.dto.TagWithoutPollsDTO;
import com.implementation.PollingApp.service.TagService;
import com.implementation.PollingApp.util.ApiResponse;

@RestController
@RequestMapping("/api/tag")
public class TagController {

        @Autowired
        private TagService tagService;

        @PostMapping("/createTag/{name}")
        ResponseEntity<ApiResponse<TagWithoutPollsDTO>> createTag(@PathVariable String name) {
                return ResponseEntity.ok(new ApiResponse<TagWithoutPollsDTO>(tagService.createTag(name), "Tag created successfully"));
        }
}