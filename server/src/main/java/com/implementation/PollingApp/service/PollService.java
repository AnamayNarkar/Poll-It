// package com.implementation.PollingApp.service;

// import java.util.Date;
// import java.util.Collections;
// import java.util.List;
// import java.util.Vector;
// import java.util.stream.Collectors;
// import java.util.stream.Stream;

// import org.bson.types.ObjectId;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.implementation.PollingApp.dto.OptionResponseDTO;
// import com.implementation.PollingApp.dto.PollEntryDTO;
// import com.implementation.PollingApp.dto.PollResponseDTO;
// import com.implementation.PollingApp.dto.TagWithouPollsDTO;
// import com.implementation.PollingApp.entity.OptionEntity;
// import com.implementation.PollingApp.entity.PollEntity;
// import com.implementation.PollingApp.entity.TagEntity;
// import com.implementation.PollingApp.entity.UserEntity;
// import com.implementation.PollingApp.entity.VoteEntity;
// import com.implementation.PollingApp.exception.custom.CannotVoteException;
// import
// com.implementation.PollingApp.exception.custom.InternalServerErrorException;
// import
// com.implementation.PollingApp.exception.custom.PollExpirationException;
// import
// com.implementation.PollingApp.exception.custom.ResourceNotFoundException;
// import com.implementation.PollingApp.repository.OptionRepository;
// import com.implementation.PollingApp.repository.PollRepository;
// import com.implementation.PollingApp.repository.TagRepository;
// import com.implementation.PollingApp.repository.UserRepository;
// import com.implementation.PollingApp.repository.VoteRepository;

// @Service
// public class PollService {

// @Autowired
// private PollRepository pollRepository;

// @Autowired
// private OptionRepository optionRepository;

// @Autowired
// private UserRepository userRepository;

// @Autowired
// private VoteRepository voteRepository;

// @Autowired
// private TagRepository tagRepository;

// public PollResponseDTO createPoll(PollEntryDTO pollEntryDTO, String username)
// {
// try {
// int totalTags = pollEntryDTO.getExistingTags().size() +
// pollEntryDTO.getNewTags().size();
// if (totalTags < 1 || totalTags > 3) {
// throw new IllegalArgumentException("Tags should be between 1 and 3");
// }

// if (pollEntryDTO.getExpirationDateTime().before(new Date())) {
// throw new PollExpirationException("Expiration date should be in the future");
// }

// UserEntity user = userRepository.findByUsername(username);
// if (user == null) {
// throw new ResourceNotFoundException("User " + username + " not found");
// }

// List<TagWithouPollsDTO> existingTags =
// tagRepository.findAllByIdWithoutPollIdsIn(pollEntryDTO.getExistingTags());
// if (existingTags.size() != pollEntryDTO.getExistingTags().size()) {
// throw new ResourceNotFoundException("One or more existing tags not found");
// }

// List<TagEntity> duplicateTags =
// tagRepository.findAllByNameIn(pollEntryDTO.getNewTags());
// if (!duplicateTags.isEmpty()) {
// throw new IllegalArgumentException("Some new tags already exist");
// }

// Vector<TagEntity> newTags =
// pollEntryDTO.getNewTags().stream().map(TagEntity::new).collect(Collectors.toCollection(Vector::new));
// tagRepository.saveAll(newTags);

// Vector<OptionEntity> options =
// pollEntryDTO.getOptions().stream().map(OptionEntity::new).collect(Collectors.toCollection(Vector::new));
// optionRepository.saveAll(options);

// Vector<ObjectId> optionIds =
// options.stream().map(OptionEntity::getId).collect(Collectors.toCollection(Vector::new));

// // Vector<ObjectId> allTagIds =
// // Stream.concat(existingTags.stream().map(TagWithouPollsDTO::getId),
// //
// newTags.stream().map(TagEntity::getId)).collect(Collectors.toCollection(Vector::new));

// PollEntity poll = new PollEntity(pollEntryDTO.getQuestion(), username,
// optionIds, pollEntryDTO.getExpirationDateTime(), allTagIds);

// pollRepository.save(poll);

// for (TagEntity newTag : newTags) {
// newTag.getPollIds().add(poll.getId());
// }

// for (TagWithouPollsDTO existingTag : existingTags) {
// // TagEntity tagEntity =
// // tagRepository.findById(existingTag.getId()).orElseThrow(() -> new
// // ResourceNotFoundException("Tag not found: " + existingTag.getId()));
// // tagEntity.getPollIds().add(poll.getId());
// // tagRepository.save(tagEntity);
// }

// tagRepository.saveAll(newTags);

// user.getCreatedPolls().add(poll.getId());
// userRepository.save(user);

// return new PollResponseDTO(poll.getId().toHexString(), poll.getQuestion(),
// poll.getCreatedBy(), poll.getCreationDateTime(),
// poll.getExpirationDateTime(), options.stream().map(option -> new
// OptionResponseDTO(option.getId().toHexString(), option.getOption(),
// option.getVoteCount())).collect(Collectors.toCollection(Vector::new)));
// } catch (ResourceNotFoundException | PollExpirationException e) {
// throw e;
// } catch (Exception e) {
// System.out.println(e.getMessage());
// throw new InternalServerErrorException("Error while creating poll");
// }
// }

// public List<PollResponseDTO> getAllPollsFromUser(String username) {
// try {
// UserEntity userEntity = userRepository.findByUsername(username);
// if (userEntity == null) {
// throw new ResourceNotFoundException(username + " not found");
// }

// List<PollEntity> polls = pollRepository.findByCreatedBy(username);
// List<PollResponseDTO> finalResponse = new Vector<>();

// for (PollEntity poll : polls) {
// List<OptionResponseDTO> options = new Vector<>();
// for (ObjectId optionId : poll.getOptions()) {
// OptionResponseDTO optionResponseDTO =
// optionRepository.findByIdWithoutVotes(optionId).orElseThrow(() -> new
// ResourceNotFoundException("Option with ID " + optionId + " not found"));
// options.add(optionResponseDTO);
// }
// finalResponse.add(new PollResponseDTO(poll.getId().toHexString(),
// poll.getQuestion(), poll.getCreatedBy(), poll.getCreationDateTime(),
// poll.getExpirationDateTime(), options));
// }

// return finalResponse.isEmpty() ? Collections.emptyList() : finalResponse;

// } catch (ResourceNotFoundException e) {
// throw e;
// } catch (Exception e) {
// throw new InternalServerErrorException("Error while fetching polls");
// }
// }

// public OptionResponseDTO vote(String pollId, String optionId, String
// username) {
// try {
// PollEntity poll = pollRepository.findById(new
// ObjectId(pollId)).orElseThrow(() -> new ResourceNotFoundException("Poll not
// found"));

// if (poll.getExpirationDateTime() == null ||
// poll.getExpirationDateTime().before(new java.util.Date())) {
// throw new PollExpirationException("Poll has expired");
// }

// UserEntity user = userRepository.findByUsername(username);
// if (user == null) {
// throw new ResourceNotFoundException("User " + username + " not found");
// }

// if (user.getVotedPolls().contains(new ObjectId(pollId))) {
// throw new CannotVoteException("User " + username + " has already voted for
// this poll");
// }

// if (user.getCreatedPolls().contains(new ObjectId(pollId))) {
// throw new CannotVoteException("User " + username + " cannot vote for a poll
// created by them");
// }

// OptionEntity option = optionRepository.findById(new
// ObjectId(optionId)).orElseThrow(() -> new ResourceNotFoundException("Option
// not found"));

// VoteEntity vote = new VoteEntity(option.getId(), username);

// voteRepository.save(vote);

// option.setVoteCount(option.getVoteCount() + 1);

// option.getVotes().add(vote.getId());

// optionRepository.save(option);

// user.getVotedPolls().add(new ObjectId(pollId));

// userRepository.save(user);

// return new OptionResponseDTO(option.getId().toHexString(),
// option.getOption(), option.getVoteCount());

// } catch (ResourceNotFoundException e) {
// throw e;
// } catch (CannotVoteException e) {
// throw e;
// } catch (PollExpirationException e) {
// throw e;
// } catch (Exception e) {
// throw new InternalServerErrorException("Error while casting vote");
// }
// }
// }