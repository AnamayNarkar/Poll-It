package com.implementation.PollingApp.service;

import java.sql.Date;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

import com.implementation.PollingApp.dto.PollEntryDTO;
import com.implementation.PollingApp.entity.OptionEntity;
import com.implementation.PollingApp.entity.PollEntity;
import com.implementation.PollingApp.entity.UserEntity;
import com.implementation.PollingApp.entity.VoteEntity;
import com.implementation.PollingApp.exception.custom.AlreadyVotedException;
import com.implementation.PollingApp.exception.custom.InternalServerErrorException;
import com.implementation.PollingApp.exception.custom.PollExpirationException;
import com.implementation.PollingApp.exception.custom.ResourceNotFoundException;
import com.implementation.PollingApp.repository.OptionRepository;
import com.implementation.PollingApp.repository.PollRepository;
import com.implementation.PollingApp.repository.UserRepository;
import com.implementation.PollingApp.repository.VoteRepository;

@Service
public class PollService {

        @Autowired
        private PollRepository pollRepository;

        @Autowired
        private OptionRepository optionRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private VoteRepository voteRepository;

        // @Transactional
        public PollEntity createPoll(PollEntryDTO pollEntryDTO, String username) {
                try {
                        if (pollEntryDTO.getExpirationDateTime().before(new Date(System.currentTimeMillis()))) {
                                throw new PollExpirationException("Expiration date should be in the future");
                        }

                        List<OptionEntity> savedOptionEntities = pollEntryDTO.getOptions().stream().map(OptionEntity::new).collect(Collectors.toList());
                        savedOptionEntities = optionRepository.saveAll(savedOptionEntities);
                        PollEntity pollEntity = new PollEntity(pollEntryDTO.getQuestion(), username, new Vector<>(savedOptionEntities), pollEntryDTO.getExpirationDateTime());
                        UserEntity userEntity = userRepository.findByUsername(username);
                        if (userEntity == null) {
                                throw new ResourceNotFoundException(username + " not found");
                        }
                        userEntity.getPolls().add(new ObjectId(pollEntity.getId()));
                        userRepository.save(userEntity);
                        return pollRepository.save(pollEntity);
                } catch (ResourceNotFoundException e) {
                        throw e;
                } catch (PollExpirationException e) {
                        throw e;
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        throw new InternalServerErrorException("Error while creating poll");
                }
        }

        public List<PollEntity> getAllPollsFromUser(String username) {

                try {

                        UserEntity userEntity = userRepository.findByUsername(username);
                        if (userEntity == null) {
                                throw new ResourceNotFoundException(username + " not found");
                        }
                        return pollRepository.findByCreatedBy(username);
                } catch (ResourceNotFoundException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalServerErrorException("Error while fetching polls");
                }
        }

        // @Transactional
        public String vote(String pollId, String optionId, String username) {
                try {
                        PollEntity poll = pollRepository.findById(new ObjectId(pollId)).orElseThrow(() -> new ResourceNotFoundException("Poll not found"));

                        if (poll.getExpirationDateTime() == null || poll.getExpirationDateTime().before(new java.util.Date())) {
                                throw new PollExpirationException("Poll has expired");
                        }

                        UserEntity user = userRepository.findByUsername(username);
                        if (user == null) {
                                throw new ResourceNotFoundException("User " + username + " not found");
                        }

                        for (OptionEntity option : poll.getOptions()) {
                                List<ObjectId> voteIds = option.getVotes();
                                if (voteIds != null) {
                                        List<VoteEntity> existingVotes = voteRepository.findAllById(voteIds);

                                        for (VoteEntity vote : existingVotes) {
                                                if (vote.getUsername().equals(username)) {
                                                        throw new AlreadyVotedException("User has already voted for this poll");
                                                }
                                        }
                                } else {
                                        option.setVotes(new Vector<>());
                                }
                        }

                        OptionEntity selectedOption = optionRepository.findById(new ObjectId(optionId)).orElseThrow(() -> new ResourceNotFoundException("Option not found"));

                        VoteEntity newVote = new VoteEntity(optionId, username);
                        selectedOption.getVotes().add(new ObjectId(newVote.getId()));
                        selectedOption.setVoteCount(selectedOption.getVoteCount() + 1);

                        optionRepository.save(selectedOption);
                        voteRepository.save(newVote);

                        return "Voted for option ID: " + selectedOption.getId() + " with text: " + selectedOption.getOption();
                } catch (ResourceNotFoundException e) {
                        throw e;
                } catch (AlreadyVotedException e) {
                        throw e;
                } catch (PollExpirationException e) {
                        throw e;
                } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage());
                        throw new InternalServerErrorException("Error while casting vote");
                }
        }

}
