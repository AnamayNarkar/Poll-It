package com.implementation.PollingApp.controller;

// import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.implementation.PollingApp.dto.PollEntryDTO;
import com.implementation.PollingApp.entity.PollEntity;
import com.implementation.PollingApp.security.authObjects.CustomSessionAuthenticationObject;
import com.implementation.PollingApp.service.PollService;
import com.implementation.PollingApp.util.ApiResponse;

import org.springframework.web.bind.annotation.RequestMapping;
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
        public ResponseEntity<ApiResponse<PollEntity>> createPoll(@RequestBody PollEntryDTO pollEntryDTO) {
                CustomSessionAuthenticationObject authentication = (CustomSessionAuthenticationObject) SecurityContextHolder.getContext().getAuthentication();
                return ResponseEntity.ok(new ApiResponse<PollEntity>(pollService.createPoll(pollEntryDTO, authentication.getUsername()), "Poll created successfully"));
        }
}
