package com.implementation.PollingApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.implementation.PollingApp.dto.TagWithouPollsDTO;
import com.implementation.PollingApp.service.TagService;
import com.implementation.PollingApp.util.ApiResponse;

@RestController
@RequestMapping("/api/tag")
public class TagController {

        @Autowired
        private TagService tagService;

        @PostMapping("/createTag/{name}")
        ResponseEntity<ApiResponse<TagWithouPollsDTO>> createTag(@PathVariable String name) {
                return ResponseEntity.ok(new ApiResponse<TagWithouPollsDTO>(tagService.createTag(name), "Tag created successfully"));
        }

        @GetMapping("/getTagsLike/{name}")
        ResponseEntity<ApiResponse<List<TagWithouPollsDTO>>> getTagsLike(@PathVariable String name) {
                return ResponseEntity.ok(new ApiResponse<List<TagWithouPollsDTO>>(tagService.getTagsLike(name), "Tags fetched successfully"));
        }
}