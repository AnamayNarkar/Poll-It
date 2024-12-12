package com.implementation.PollingApp.service;

import java.util.Date;
import java.util.Vector;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.implementation.PollingApp.dto.OptionResponseDTO;
import com.implementation.PollingApp.dto.PollEntryDTO;
import com.implementation.PollingApp.dto.PollResponseDTO;
import com.implementation.PollingApp.entity.OptionEntity;
import com.implementation.PollingApp.entity.PollEntity;
import com.implementation.PollingApp.entity.SessionValueEntity;
import com.implementation.PollingApp.entity.TagEntity;
import com.implementation.PollingApp.entity.UserEntity;
import com.implementation.PollingApp.entity.VoteEntity;
import com.implementation.PollingApp.exception.custom.CannotVoteException;
import com.implementation.PollingApp.exception.custom.InternalServerErrorException;
import com.implementation.PollingApp.exception.custom.PollExpirationException;
import com.implementation.PollingApp.exception.custom.ResourceNotFoundException;
import com.implementation.PollingApp.repository.OptionRepository;
import com.implementation.PollingApp.repository.PollRepository;
import com.implementation.PollingApp.repository.TagRepository;
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

        @Autowired
        private TagRepository tagRepository;

        public PollResponseDTO createPoll(SessionValueEntity sve, PollEntryDTO pollEntryDTO) {
                try {

                        if (pollEntryDTO.getQuestion().length() < 1 || pollEntryDTO.getQuestion().length() > 200) {
                                throw new IllegalArgumentException("Question should be between 1 and 200 characters");
                        }

                        if (pollEntryDTO.getTags().size() < 1 || pollEntryDTO.getTags().size() > 3) {
                                throw new IllegalArgumentException("Tags should be between 1 and 3");
                        }

                        if (pollEntryDTO.getExpirationDateTime().before(new Date())) {
                                throw new PollExpirationException("Expiration date should be in the future");
                                // a sample expiration date toenter injson format is "2021-12-31T23:59:59.999Z"
                        }

                        UserEntity user = userRepository.findByUsername(sve.getUsername());
                        if (user == null) {
                                throw new ResourceNotFoundException("User " + sve.getUsername() + " not found");
                        }

                        Vector<ObjectId> listOfObjectIdsOfAllTags = pollEntryDTO.getTags().stream().map((singleTagWithoutPollsDTO) -> new ObjectId(singleTagWithoutPollsDTO.getId())).collect(Collectors.toCollection(Vector::new));

                        Vector<TagEntity> allTagEntities = tagRepository.findAllById(listOfObjectIdsOfAllTags).stream().collect(Collectors.toCollection(Vector::new));

                        if (allTagEntities.size() != pollEntryDTO.getTags().size()) {
                                throw new ResourceNotFoundException("One or more tags not found");
                        }

                        Vector<OptionEntity> allOptionEntities = pollEntryDTO.getOptions().stream().map((option) -> {
                                OptionEntity optionEntity = new OptionEntity(option);
                                optionRepository.save(optionEntity);
                                return optionEntity;
                        }).collect(Collectors.toCollection(Vector::new));

                        Vector<ObjectId> listOfObjectIdsOfAllOptions = allOptionEntities.stream().map((optionEntity) -> optionEntity.getId()).collect(Collectors.toCollection(Vector::new));

                        Vector<OptionResponseDTO> allOptionResponseDTOs = allOptionEntities.stream().map((optionEntity) -> new OptionResponseDTO(optionEntity.getId().toHexString(), optionEntity.getOption(), optionEntity.getVoteCount())).collect(Collectors.toCollection(Vector::new));

                        PollEntity poll = new PollEntity(pollEntryDTO.getQuestion(), sve.getUsername(), listOfObjectIdsOfAllOptions, pollEntryDTO.getExpirationDateTime(), listOfObjectIdsOfAllTags);

                        pollRepository.save(poll);

                        user.getCreatedPolls().add(poll.getId());

                        userRepository.save(user);

                        for (TagEntity tag : allTagEntities) {
                                tag.getPollIds().add(poll.getId());
                                tagRepository.save(tag);
                        }

                        return new PollResponseDTO(poll, allOptionResponseDTOs, pollEntryDTO.getTags());

                } catch (ResourceNotFoundException | PollExpirationException e) {
                        throw e;
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        throw new InternalServerErrorException("Error while creating poll");
                }
        }

        public OptionResponseDTO vote(SessionValueEntity sve, String pollId, String optionId) {

                try {

                        UserEntity user = userRepository.findByUsername(sve.getUsername());
                        if (user == null) {
                                throw new ResourceNotFoundException("User " + sve.getUsername() + " not found");
                        }

                        if (user.getVotedPolls().containsKey(new ObjectId(pollId))) {
                                throw new CannotVoteException("User has already voted for this poll");
                        }

                        PollEntity poll = pollRepository.findById(new ObjectId(pollId)).orElseThrow(() -> new ResourceNotFoundException("Poll not Found"));

                        if (poll.getExpirationDateTime().before(new Date())) {
                                throw new PollExpirationException("Poll has expired");
                        }

                        poll.setTotalVoteCount(poll.getTotalVoteCount() + 1);

                        OptionEntity option = optionRepository.findById(new ObjectId(optionId)).orElseThrow(() -> new ResourceNotFoundException("Option not found"));

                        VoteEntity vote = new VoteEntity(new ObjectId(optionId), sve.getUsername());

                        pollRepository.save(poll);

                        voteRepository.save(vote);

                        option.getVotes().add(vote.getId());

                        option.setVoteCount(option.getVoteCount() + 1);

                        optionRepository.save(option);

                        user.getVotedPolls().put(new ObjectId(pollId), new ObjectId(optionId));

                        userRepository.save(user);

                        return new OptionResponseDTO(option.getId().toHexString(), option.getOption(), option.getVoteCount());

                } catch (ResourceNotFoundException | PollExpirationException | CannotVoteException e) {
                        throw e;
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        throw new InternalServerErrorException("Error while casting vote");
                }

        }

}