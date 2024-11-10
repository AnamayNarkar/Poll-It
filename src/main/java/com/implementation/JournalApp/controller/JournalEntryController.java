package com.implementation.JournalApp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.implementation.JournalApp.dto.JournalEntryDTO;
import com.implementation.JournalApp.entity.JournalEntryEntity;

@RestController
@RequestMapping("/api/journal")
public class JournalEntryController {

        @PostMapping("/createJournalEntry")
        public String createJournalEntry(@RequestBody JournalEntryDTO journalEntryDTO) {
                System.out.println(journalEntryDTO);
                return "Journal Entry created";
        }

        @GetMapping("/getJournalEntriesForUser")
        public String getJournalEntriesForUser() {
                return "Journal Entries for user ";
        }

        @PutMapping("/updateJournalEntry")
        public String updateJournalEntryById(@RequestBody JournalEntryEntity journalEntryEntity) {
                return "Journal Entry updated with ID: " + journalEntryEntity.getId();
        }

        @GetMapping("/getJournalEntryById/{entryId}")
        public String getJournalEntryById(@PathVariable Long entryId) {
                return "Journal Entry with ID: " + entryId;
        }

        @DeleteMapping("/deleteJournalEntryById/{entryId}")
        public String deleteJournalEntryById(@PathVariable Long entryId) {
                return "Journal Entry deleted with ID: " + entryId;
        }

}
