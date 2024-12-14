package com.implementation.PollingApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.implementation.PollingApp.dto.TagWithoutPollsDTO;
import com.implementation.PollingApp.dto.UserForDisplayingSearchResultsDTO;
import com.implementation.PollingApp.service.SearchService;
import com.implementation.PollingApp.util.ApiResponse;

@RestController
@RequestMapping("/api/search/")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/getTagsLike/{name}")
    ResponseEntity<ApiResponse<List<TagWithoutPollsDTO>>> getTagsLike(@PathVariable String name) {
        return ResponseEntity.ok(new ApiResponse<List<TagWithoutPollsDTO>>(searchService.getTagsLike(name), "Tags fetched successfully"));
    }

    @GetMapping("/getUsersLike/{name}")
    ResponseEntity<ApiResponse<List<UserForDisplayingSearchResultsDTO>>> getUsersLike(@PathVariable String name) {
        return ResponseEntity.ok(new ApiResponse<List<UserForDisplayingSearchResultsDTO>>(searchService.getUsersLike(name), "Users fetched successfully"));
    }

}
