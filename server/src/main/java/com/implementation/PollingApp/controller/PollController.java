package com.implementation.PollingApp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.implementation.PollingApp.dto.OptionResponseDTO;
import com.implementation.PollingApp.dto.PollEntryDTO;
import com.implementation.PollingApp.dto.PollResponseDTO;
import com.implementation.PollingApp.security.authObjects.CustomSessionAuthenticationObject;
import com.implementation.PollingApp.service.PollService;
import com.implementation.PollingApp.util.ApiResponse;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

// import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
// import java.util.List;

@RestController
@RequestMapping("/api/poll")
public class PollController {

        @Autowired
        private PollService pollService;

        @PostMapping("/createPoll")
        public ResponseEntity<ApiResponse<PollResponseDTO>> createPoll(@RequestBody PollEntryDTO pollEntryDTO) {
                CustomSessionAuthenticationObject authentication = (CustomSessionAuthenticationObject) SecurityContextHolder.getContext().getAuthentication();
                return ResponseEntity.ok(new ApiResponse<PollResponseDTO>(pollService.createPoll(authentication.getSessionValueEntity(), pollEntryDTO), "Poll created successfully"));
        }

        // @GetMapping("/getAllPollsFromUser")
        // public ResponseEntity<ApiResponse<List<PollResponseDTO>>>
        // getAllPollsFromUser() {
        // return ResponseEntity.ok(new
        // ApiResponse<List<PollResponseDTO>>(pollService.getAllPollsFromUser(((CustomSessionAuthenticationObject)
        // SecurityContextHolder.getContext().getAuthentication()).getUsername()),
        // "Polls fetched successfully"));
        // }

        // @PostMapping("/vote/{pollId}/{optionId}")
        // public ResponseEntity<ApiResponse<OptionResponseDTO>> vote(@PathVariable
        // String pollId, @PathVariable String optionId) {
        // CustomSessionAuthenticationObject authentication =
        // (CustomSessionAuthenticationObject)
        // SecurityContextHolder.getContext().getAuthentication();
        // return ResponseEntity.ok(new
        // ApiResponse<OptionResponseDTO>(pollService.vote(pollId, optionId,
        // authentication.getUsername()), "Voted successfully"));
        // }

}
