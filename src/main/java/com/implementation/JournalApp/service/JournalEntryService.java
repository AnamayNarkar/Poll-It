package com.implementation.JournalApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.implementation.JournalApp.dto.JournalEntryDTO;
import com.implementation.JournalApp.entity.JournalEntryEntity;
import com.implementation.JournalApp.entity.UserEntity;
import com.implementation.JournalApp.exception.custom.InternalServerErrorException;
import com.implementation.JournalApp.exception.custom.ResourceNotFoundException;
import com.implementation.JournalApp.exception.custom.ValidationException;
import com.implementation.JournalApp.repository.JournalEntryRepository;
import com.implementation.JournalApp.repository.UserRepository;

@Service
public class JournalEntryService {

        @Autowired
        private JournalEntryRepository journalEntryRepository;

        @Autowired
        private UserRepository userRepository;

        public JournalEntryEntity createJournalEntry(JournalEntryDTO journalEntryDTO, String username) {
                try {
                        if (journalEntryDTO == null || username == null || username.isEmpty()) {
                                throw new ValidationException("Invalid input: journal entry data or username is missing");
                        }

                        UserEntity userEntity = userRepository.findByusername(username);
                        if (userEntity == null) {
                                throw new ResourceNotFoundException("User with username " + username + " not found");
                        }

                        JournalEntryEntity journalEntryEntity = new JournalEntryEntity(journalEntryDTO.getTitle(), journalEntryDTO.getContent());
                        JournalEntryEntity savedObject = journalEntryRepository.save(journalEntryEntity);

                        userEntity.getJournalEntries().add(savedObject);
                        userRepository.save(userEntity);

                        return savedObject;

                } catch (ResourceNotFoundException | ValidationException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalServerErrorException("An error occurred while creating a journal entry");
                }
        }

}
