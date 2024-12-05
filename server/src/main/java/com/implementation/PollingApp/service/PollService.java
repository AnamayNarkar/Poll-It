package com.implementation.PollingApp.service;

import java.util.Date;
import java.util.Collections;
import java.util.List;
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

        public PollResponseDTO createPoll(PollEntryDTO pollEntryDTO, String username) {
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

                        UserEntity user = userRepository.findByUsername(username);
                        if (user == null) {
                                throw new ResourceNotFoundException("User " + username + " not found");
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

                        PollEntity poll = new PollEntity(pollEntryDTO.getQuestion(), username, listOfObjectIdsOfAllOptions, pollEntryDTO.getExpirationDateTime(), listOfObjectIdsOfAllTags);

                        pollRepository.save(poll);

                        user.getCreatedPolls().add(poll.getId());

                        userRepository.save(user);

                        for (TagEntity tag : allTagEntities) {
                                tag.getPollIds().add(poll.getId());
                                tagRepository.save(tag);
                        }

                        return new PollResponseDTO(poll.getId().toHexString(), poll.getQuestion(), poll.getCreatedBy(), poll.getCreationDateTime(), poll.getExpirationDateTime(), allOptionResponseDTOs);

                } catch (ResourceNotFoundException | PollExpirationException e) {
                        throw e;
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        throw new InternalServerErrorException("Error while creating poll");
                }
        }

        public List<PollResponseDTO> getAllPollsFromUser(String username) {
                try {
                        UserEntity userEntity = userRepository.findByUsername(username);
                        if (userEntity == null) {
                                throw new ResourceNotFoundException(username + " not found");
                        }

                        List<PollEntity> polls = pollRepository.findByCreatedBy(username);
                        List<PollResponseDTO> finalResponse = new Vector<>();

                        for (PollEntity poll : polls) {
                                List<OptionResponseDTO> options = new Vector<>();
                                for (ObjectId optionId : poll.getOptions()) {
                                        OptionResponseDTO optionResponseDTO = optionRepository.findByIdWithoutVotes(optionId).orElseThrow(() -> new ResourceNotFoundException("Option with ID " + optionId + " not found"));
                                        options.add(optionResponseDTO);
                                }
                                finalResponse.add(new PollResponseDTO(poll.getId().toHexString(), poll.getQuestion(), poll.getCreatedBy(), poll.getCreationDateTime(), poll.getExpirationDateTime(), options));
                        }

                        return finalResponse.isEmpty() ? Collections.emptyList() : finalResponse;

                } catch (ResourceNotFoundException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalServerErrorException("Error while fetching polls");
                }
        }

        public OptionResponseDTO vote(String pollId, String optionId, String username) {
                try {
                        PollEntity poll = pollRepository.findById(new ObjectId(pollId)).orElseThrow(() -> new ResourceNotFoundException("Poll not found"));

                        if (poll.getExpirationDateTime() == null || poll.getExpirationDateTime().before(new java.util.Date())) {
                                throw new PollExpirationException("Poll has expired");
                        }

                        UserEntity user = userRepository.findByUsername(username);
                        if (user == null) {
                                throw new ResourceNotFoundException("User " + username + " not found");
                        }

                        if (user.getVotedPolls().contains(new ObjectId(pollId))) {
                                throw new CannotVoteException("User " + username + " has already voted forthis poll");
                        }

                        if (user.getCreatedPolls().contains(new ObjectId(pollId))) {
                                throw new CannotVoteException("User " + username + " cannot vote for a pollcreated by them");
                        }

                        OptionEntity option = optionRepository.findById(new ObjectId(optionId)).orElseThrow(() -> new ResourceNotFoundException("Optionnot found"));

                        VoteEntity vote = new VoteEntity(option.getId(), username);

                        voteRepository.save(vote);

                        option.setVoteCount(option.getVoteCount() + 1);

                        option.getVotes().add(vote.getId());

                        optionRepository.save(option);

                        user.getVotedPolls().add(new ObjectId(pollId));

                        userRepository.save(user);

                        return new OptionResponseDTO(option.getId().toHexString(), option.getOption(), option.getVoteCount());

                } catch (ResourceNotFoundException e) {
                        throw e;
                } catch (CannotVoteException e) {
                        throw e;
                } catch (PollExpirationException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalServerErrorException("Error while casting vote");
                }
        }
}