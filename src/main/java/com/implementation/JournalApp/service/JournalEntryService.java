package com.implementation.JournalApp.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

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

        // @Transactional
        public JournalEntryEntity createJournalEntry(JournalEntryDTO journalEntryDTO, String username) {
                try {
                        if (journalEntryDTO == null || username == null || username.isEmpty() || journalEntryDTO.getTitle() == null || journalEntryDTO.getTitle().isEmpty() || journalEntryDTO.getContent() == null || journalEntryDTO.getContent().isEmpty()) {
                                throw new ValidationException("Invalid input: journal entry data or username is missing");
                        }

                        UserEntity userEntity = userRepository.findByUsername(username);
                        if (userEntity == null) {
                                throw new ResourceNotFoundException("User with username " + username + " not found");
                        }

                        JournalEntryEntity journalEntryEntity = new JournalEntryEntity(journalEntryDTO.getTitle(), journalEntryDTO.getContent());
                        JournalEntryEntity savedObject = journalEntryRepository.save(journalEntryEntity);

                        userEntity.getJournalEntries().add(savedObject);
                        userRepository.save(userEntity);

                        return savedObject;

                } catch (ResourceNotFoundException e) {
                        throw e;
                } catch (ValidationException e) {
                        throw e;
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        throw new InternalServerErrorException("An error occurred while creating a journal entry");
                }
        }

        public List<JournalEntryEntity> getJournalEntriesForUser(String username) {
                try {
                        if (username == null || username.isEmpty()) {
                                throw new ValidationException("Invalid input: username is missing");
                        }

                        UserEntity userEntity = userRepository.findByUsername(username);
                        if (userEntity == null) {
                                throw new ResourceNotFoundException("User with username " + username + " not found");
                        }

                        return userEntity.getJournalEntries();

                } catch (ResourceNotFoundException e) {
                        throw e;
                } catch (ValidationException e) {
                        throw e;
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        throw new InternalServerErrorException("An error occurred while fetching journal entries");
                }
        }

        public JournalEntryEntity getJournalEntryById(String id) {
                try {
                        if (id == null) {
                                throw new ValidationException("Invalid input: journal entry id is missing");
                        }

                        ObjectId objectId = new ObjectId(id);

                        JournalEntryEntity journalEntryEntity = journalEntryRepository.findById(objectId).orElse(null);

                        if (journalEntryEntity == null) {
                                throw new ResourceNotFoundException("Journal entry with id " + id + " not found");
                        }

                        return journalEntryEntity;

                } catch (ResourceNotFoundException e) {
                        throw e;
                } catch (ValidationException e) {
                        throw e;
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        throw new InternalServerErrorException("An error occurred while fetching journal entry");
                }
        }
}