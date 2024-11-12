package com.implementation.JournalApp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;

import com.implementation.JournalApp.dto.JournalEntryDTO;
import com.implementation.JournalApp.entity.JournalEntryEntity;
import com.implementation.JournalApp.service.JournalEntryService;
import com.implementation.JournalApp.util.ApiResponse;
import com.implementation.JournalApp.security.authObjects.CustomSessionAuthenticationObject;

@RestController
@RequestMapping("/api/journal")
public class JournalEntryController {

        @Autowired
        private JournalEntryService journalEntryService;

        @PostMapping("/createJournalEntry")
        public ResponseEntity<ApiResponse<JournalEntryEntity>> createJournalEntry(@RequestBody JournalEntryDTO journalEntryDTO) {
                CustomSessionAuthenticationObject authentication = (CustomSessionAuthenticationObject) SecurityContextHolder.getContext().getAuthentication();
                return ResponseEntity.ok(new ApiResponse<JournalEntryEntity>(journalEntryService.createJournalEntry(journalEntryDTO, authentication.getUsername()), "Journal Entry created successfully"));
        }

        @GetMapping("/getJournalEntriesForUser")
        public ResponseEntity<ApiResponse<List<JournalEntryEntity>>> getJournalEntriesForUser() {
                CustomSessionAuthenticationObject authentication = (CustomSessionAuthenticationObject) SecurityContextHolder.getContext().getAuthentication();
                return ResponseEntity.ok(new ApiResponse<List<JournalEntryEntity>>(journalEntryService.getJournalEntriesForUser(authentication.getUsername()), "Journal Entries retrieved successfully"));
        }

        @PutMapping("/updateJournalEntry")
        public String updateJournalEntryById(@RequestBody JournalEntryEntity journalEntryEntity) {
                return "Journal Entry updated with ID: " + journalEntryEntity.getId();
        }

        @GetMapping("/getJournalEntryById/{entryId}")
        public ResponseEntity<ApiResponse<JournalEntryEntity>> getJournalEntryById(@PathVariable String entryId) {
                return ResponseEntity.ok(new ApiResponse<JournalEntryEntity>(journalEntryService.getJournalEntryById(entryId), "Journal Entry retrieved successfully"));
        }

        @DeleteMapping("/deleteJournalEntryById/{entryId}")
        public String deleteJournalEntryById(@PathVariable Long entryId) {
                return "Journal Entry deleted with ID: " + entryId;
        }

}
