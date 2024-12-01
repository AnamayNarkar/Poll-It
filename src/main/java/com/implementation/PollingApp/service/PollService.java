package com.implementation.PollingApp.service;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.implementation.PollingApp.dto.PollEntryDTO;
import com.implementation.PollingApp.entity.OptionEntity;
import com.implementation.PollingApp.entity.PollEntity;
import com.implementation.PollingApp.entity.UserEntity;
import com.implementation.PollingApp.exception.custom.InternalServerErrorException;
import com.implementation.PollingApp.exception.custom.ResourceNotFoundException;
import com.implementation.PollingApp.repository.OptionRepository;
import com.implementation.PollingApp.repository.PollRepository;
import com.implementation.PollingApp.repository.UserRepository;

@Service
public class PollService {

        @Autowired
        private PollRepository pollRepository;

        @Autowired
        private OptionRepository optionRepository;

        @Autowired
        private UserRepository userRepository;

        @Transactional
        public PollEntity createPoll(PollEntryDTO pollEntryDTO, String username) {
                try {
                        List<OptionEntity> savedOptionEntities = pollEntryDTO.getOptions().stream().map(OptionEntity::new).collect(Collectors.toList());
                        savedOptionEntities = optionRepository.saveAll(savedOptionEntities);
                        PollEntity pollEntity = new PollEntity(pollEntryDTO.getQuestion(), username, new Vector<>(savedOptionEntities));
                        UserEntity userEntity = userRepository.findByUsername(username);
                        userEntity.getPolls().add(new ObjectId(pollEntity.getId()));
                        userRepository.save(userEntity);
                        return pollRepository.save(pollEntity);
                } catch (Exception e) {
                        throw new InternalServerErrorException("Error while creating poll");
                }
        }

        public List<PollEntity> getAllPollsFromUser(String username) {

                UserEntity userEntity = userRepository.findByUsername(username);
                if (userEntity == null) {
                        throw new ResourceNotFoundException(username + " not found");
                }
                return pollRepository.findByCreatedBy(username);
        }
}
