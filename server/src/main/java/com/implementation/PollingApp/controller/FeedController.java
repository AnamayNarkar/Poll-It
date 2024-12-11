package com.implementation.PollingApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.implementation.PollingApp.dto.PollResponseDTO;
import com.implementation.PollingApp.dto.UserDataWithFollowedTagsDTO;
import com.implementation.PollingApp.dto.UserWhenSearchedDTO;
import com.implementation.PollingApp.security.authObjects.CustomSessionAuthenticationObject;
import com.implementation.PollingApp.service.FeedService;
import com.implementation.PollingApp.util.ApiResponse;

@RestController
@RequestMapping("/api/feed/")
public class FeedController {

        @Autowired
        private FeedService feedService;

        @GetMapping("/getUserData")
        public ResponseEntity<ApiResponse<UserDataWithFollowedTagsDTO>> getUserData() {
                try {
                        Thread.sleep(1000);
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                CustomSessionAuthenticationObject authentication = (CustomSessionAuthenticationObject) SecurityContextHolder.getContext().getAuthentication();
                return ResponseEntity.ok(new ApiResponse<UserDataWithFollowedTagsDTO>(feedService.getUserData(authentication.getSessionValueEntity()), "User data fetched successfully"));
        }

        @GetMapping("/home/{limit}")
        public ResponseEntity<ApiResponse<List<PollResponseDTO>>> getHomeFeed(@PathVariable Integer limit) {
                try {
                        Thread.sleep(1000);
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                CustomSessionAuthenticationObject authentication = (CustomSessionAuthenticationObject) SecurityContextHolder.getContext().getAuthentication();
                System.out.println(authentication.getSessionValueEntity().getUsername() + "is fetching home feed");
                return ResponseEntity.ok(new ApiResponse<List<PollResponseDTO>>(feedService.getHomeFeed(authentication.getSessionId(), authentication.getSessionValueEntity(), limit), "Home feed fetched successfully"));
        }

        @GetMapping("/tag/{tagName}/{page}/{limit}")
        public ResponseEntity<ApiResponse<List<PollResponseDTO>>> getTagFeed(@PathVariable String tagName, @PathVariable Integer page, @PathVariable Integer limit) {
                try {
                        Thread.sleep(1000);
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                CustomSessionAuthenticationObject authentication = (CustomSessionAuthenticationObject) SecurityContextHolder.getContext().getAuthentication();
                return ResponseEntity.ok(new ApiResponse<List<PollResponseDTO>>(feedService.getTagFeed(authentication.getSessionId(), authentication.getSessionValueEntity(), tagName, page, limit), "Tag feed fetched successfully"));
        }

        @GetMapping("/userDataWhenSearched/{username}")
        public ResponseEntity<ApiResponse<UserWhenSearchedDTO>> getUserDataForWhenSearched(@PathVariable String username) {
                try {
                        Thread.sleep(1000);
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                CustomSessionAuthenticationObject authentication = (CustomSessionAuthenticationObject) SecurityContextHolder.getContext().getAuthentication();
                return ResponseEntity.ok(new ApiResponse<UserWhenSearchedDTO>(feedService.getUserDataForWhenSearched(authentication.getSessionId(), authentication.getSessionValueEntity(), username), "User data fetched successfully"));
        }

        @GetMapping("/user/{username}/{page}/{limit}")
        public ResponseEntity<ApiResponse<List<PollResponseDTO>>> getUserFeed(@PathVariable String username, @PathVariable Integer page, @PathVariable Integer limit) {
                try {
                        Thread.sleep(1000);
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                CustomSessionAuthenticationObject authentication = (CustomSessionAuthenticationObject) SecurityContextHolder.getContext().getAuthentication();
                return ResponseEntity.ok(new ApiResponse<List<PollResponseDTO>>(feedService.getPollsOfUser(authentication.getSessionId(), authentication.getSessionValueEntity(), username, page, limit), "User feed fetched successfully"));
        }

}