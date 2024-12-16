package com.implementation.PollingApp.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.implementation.PollingApp.dao.TagWithoutPollsDAO;
import com.implementation.PollingApp.dto.OptionResponseDTO;
import com.implementation.PollingApp.dto.PollResponseDTO;
import com.implementation.PollingApp.dto.TagWithoutPollsDTO;
import com.implementation.PollingApp.dto.UserDataWithFollowedTagsDTO;
import com.implementation.PollingApp.dto.UserWhenSearchedDTO;
import com.implementation.PollingApp.entity.FeedValueEntity;
import com.implementation.PollingApp.entity.PollEntity;
import com.implementation.PollingApp.entity.SessionValueEntity;
import com.implementation.PollingApp.entity.UserEntity;
import com.implementation.PollingApp.exception.custom.ResourceNotFoundException;
import com.implementation.PollingApp.repository.OptionRepository;
import com.implementation.PollingApp.repository.PollRepository;
import com.implementation.PollingApp.repository.RedisRepositoryForFeed;
import com.implementation.PollingApp.repository.RedisRepositoryForSessions;
import com.implementation.PollingApp.repository.TagRepository;
import com.implementation.PollingApp.repository.UserRepository;

@Service
public class FeedService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PollRepository pollRepository;
    @Autowired
    private RedisRepositoryForSessions redisRepositoryForSessions;
    @Autowired
    private RedisRepositoryForFeed redisRepositoryForFeed;
    @Autowired
    private OptionRepository optionRepository;

    public List<PollResponseDTO> getHomeFeed(String sessionKey, SessionValueEntity sve, Integer limit) {
        try {
            if (sve == null || sessionKey == null) {
                throw new IllegalArgumentException("Session details or session key cannot be null");
            }

            Boolean hasFeedState = sve.getFeedStateRedisKey() != null;
            FeedValueEntity feedValueEntity = null;

            if (hasFeedState) {
                feedValueEntity = redisRepositoryForFeed.getValue(sve.getFeedStateRedisKey());
            }

            System.out.println(feedValueEntity);

            UserEntity user = userRepository.findByUsername(sve.getUsername());
            if (user == null) {
                throw new ResourceNotFoundException("User not found for session");
            }

            if (feedValueEntity == null) {

                hasFeedState = false;
                feedValueEntity = new FeedValueEntity(new Vector<>(), new HashMap<>(), new HashSet<>());
                feedValueEntity.setFollowedTags(user.getFollowedTags().stream().map(ObjectId::toHexString).collect(Collectors.toList()));
                for (String tag : feedValueEntity.getFollowedTags()) {
                    feedValueEntity.getFeedState().put(tag, null);
                }
                feedValueEntity.setSentPostIds(new HashSet<>());
                System.out.println(feedValueEntity);
            }

            if (feedValueEntity.getFollowedTags().isEmpty()) {
                throw new ResourceNotFoundException("No tags followed by user");
            }

            List<PollResponseDTO> homeFeed = new ArrayList<>();
            Set<String> processedPollIds = new HashSet<>();
            int pollsAdded = 0;
            int followedTagsPointer = 0;

            while (pollsAdded < limit) {
                boolean hasFoundSomethingInThisIteration = false;

                for (int i = 0; i < feedValueEntity.getFollowedTags().size(); i++) {
                    String tagHex = feedValueEntity.getFollowedTags().get(followedTagsPointer);
                    ObjectId tag = new ObjectId(tagHex);

                    List<PollEntity> listOfPolls;
                    if (feedValueEntity.getFeedState().get(tagHex) != null) {
                        listOfPolls = pollRepository.findImmediatePollBefore(tag, new ObjectId(feedValueEntity.getFeedState().get(tagHex)), PageRequest.of(0, 1));
                    } else {
                        listOfPolls = pollRepository.findLatestPollForThisTag(tag, PageRequest.of(0, 1));
                    }

                    if (!listOfPolls.isEmpty()) {
                        PollEntity poll = listOfPolls.get(0);
                        String pollId = poll.getId().toHexString();

                        if (!feedValueEntity.getSentPostIds().contains(pollId)) {
                            if (!processedPollIds.contains(pollId)) {
                                processedPollIds.add(pollId);

                                feedValueEntity.getFeedState().put(tagHex, pollId);

                                List<OptionResponseDTO> optionResponseDTOs = poll.getOptions().stream().map(optionId -> optionRepository.findByIdWithoutVotes(optionId)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());

                                List<TagWithoutPollsDAO> tagWithoutPollsDAOs = tagRepository.findAllByIdWithoutPollIdsIn(poll.getTags());
                                List<TagWithoutPollsDTO> tagWithoutPollsDTOs = tagWithoutPollsDAOs.stream().map(tagDAO -> new TagWithoutPollsDTO(tagDAO.getId().toHexString(), tagDAO.getName())).collect(Collectors.toList());

                                boolean hasUserVoted = user.getVotedPolls().containsKey(poll.getId());
                                String votedOptionId = hasUserVoted ? user.getVotedPolls().get(poll.getId()).toHexString() : null;

                                homeFeed.add(new PollResponseDTO(poll, optionResponseDTOs, tagWithoutPollsDTOs, hasUserVoted, votedOptionId));
                                feedValueEntity.getSentPostIds().add(pollId);

                                pollsAdded++;
                                hasFoundSomethingInThisIteration = true;
                            }
                        } else {
                            feedValueEntity.getFeedState().put(tagHex, pollId);
                        }
                    }

                    followedTagsPointer = (followedTagsPointer + 1) % feedValueEntity.getFollowedTags().size();
                    if (pollsAdded >= limit) {
                        break;
                    }
                }

                if (!hasFoundSomethingInThisIteration) {
                    break;
                }
            }

            if (hasFeedState) {
                redisRepositoryForFeed.updateValue(sve.getFeedStateRedisKey(), feedValueEntity);
            } else {
                String redisKey = redisRepositoryForFeed.saveValue(feedValueEntity, 30);
                sve = new SessionValueEntity(sve.getId(), sve.getUsername(), sve.getRoles(), redisKey);
                redisRepositoryForSessions.updateValue(sessionKey, sve);
            }

            return homeFeed;

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unable to fetch home feed", e);
        }
    }

    public UserDataWithFollowedTagsDTO getUserData(SessionValueEntity sve) {
        UserEntity user = userRepository.findByUsername(sve.getUsername());
        List<TagWithoutPollsDAO> tagWithouPollsDAO = tagRepository.findAllByIdWithoutPollIdsIn(user.getFollowedTags());
        List<TagWithoutPollsDTO> tagsWithStringIds = tagWithouPollsDAO.stream().map(tag -> new TagWithoutPollsDTO(tag.getId().toHexString(), tag.getName())).collect(Collectors.toList());
        return new UserDataWithFollowedTagsDTO(user.getId().toHexString(), user.getUsername(), tagsWithStringIds, user.getProfilePictureURL());
    }

    public List<PollResponseDTO> getTagFeed(String sessionKey, SessionValueEntity sve, String tagName, Integer page, Integer limit) {
        TagWithoutPollsDAO tagDAO = tagRepository.findTagByNameWithoutPollIds(tagName);
        if (tagDAO == null) {
            throw new ResourceNotFoundException("Tag not found");
        }
        ObjectId tagId = tagDAO.getId();

        List<PollEntity> polls = pollRepository.findByTags(tagId, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "_id")));

        List<PollResponseDTO> pollResponseDTOs = polls.stream().map(poll -> {
            List<OptionResponseDTO> optionResponseDTOs = poll.getOptions().stream().map(optionId -> optionRepository.findByIdWithoutVotes(optionId)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
            List<TagWithoutPollsDAO> tagWithoutPollsDAOs = tagRepository.findAllByIdWithoutPollIdsIn(poll.getTags());
            List<TagWithoutPollsDTO> tagWithoutPollsDTOs = tagWithoutPollsDAOs.stream().map(tag -> new TagWithoutPollsDTO(tag.getId().toHexString(), tag.getName())).collect(Collectors.toList());
            UserEntity user = userRepository.findByUsername(sve.getUsername());
            boolean hasUserVoted = user.getVotedPolls().containsKey(poll.getId());
            String votedOptionId = hasUserVoted ? user.getVotedPolls().get(poll.getId()).toHexString() : null;
            return new PollResponseDTO(poll, optionResponseDTOs, tagWithoutPollsDTOs, hasUserVoted, votedOptionId);
        }).collect(Collectors.toList());

        return pollResponseDTOs;
    }

    public UserWhenSearchedDTO getUserDataForWhenSearched(String sessionKey, SessionValueEntity sve, String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        return new UserWhenSearchedDTO(user.getUsername(), user.getProfilePictureURL(), user.getBio());
    }

    public List<PollResponseDTO> getPollsOfUser(String sessionKey, SessionValueEntity sve, String username, Integer page, Integer limit) {

        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        List<PollEntity> polls = pollRepository.findByCreatedBy(user.getUsername(), PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "_id")));

        List<PollResponseDTO> pollResponseDTOs = polls.stream().map(poll -> {
            List<OptionResponseDTO> optionResponseDTOs = poll.getOptions().stream().map(optionId -> optionRepository.findByIdWithoutVotes(optionId)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
            List<TagWithoutPollsDAO> tagWithoutPollsDAOs = tagRepository.findAllByIdWithoutPollIdsIn(poll.getTags());
            List<TagWithoutPollsDTO> tagWithoutPollsDTOs = tagWithoutPollsDAOs.stream().map(tag -> new TagWithoutPollsDTO(tag.getId().toHexString(), tag.getName())).collect(Collectors.toList());
            UserEntity createdByUser = userRepository.findByUsername(sve.getUsername());
            boolean hasUserVoted = createdByUser.getVotedPolls().containsKey(poll.getId());
            String votedOptionId = hasUserVoted ? createdByUser.getVotedPolls().get(poll.getId()).toHexString() : null;
            return new PollResponseDTO(poll, optionResponseDTOs, tagWithoutPollsDTOs, hasUserVoted, votedOptionId);
        }).collect(Collectors.toList());

        return pollResponseDTOs;
    }

    public List<PollResponseDTO> getPopularFeed(String sessionKey, SessionValueEntity sve, Integer page, Integer limit) {

        Instant startDate = Instant.now().minus(30, ChronoUnit.DAYS);

        System.out.println(startDate);

        List<PollEntity> polls = pollRepository.findPopularPollsByDateRange(startDate, PageRequest.of(page, limit));

        System.out.println(polls);

        List<PollResponseDTO> pollResponseDTOs = polls.stream().map(poll -> {
            List<OptionResponseDTO> optionResponseDTOs = poll.getOptions().stream().map(optionId -> optionRepository.findByIdWithoutVotes(optionId)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
            List<TagWithoutPollsDAO> tagWithoutPollsDAOs = tagRepository.findAllByIdWithoutPollIdsIn(poll.getTags());
            List<TagWithoutPollsDTO> tagWithoutPollsDTOs = tagWithoutPollsDAOs.stream().map(tag -> new TagWithoutPollsDTO(tag.getId().toHexString(), tag.getName())).collect(Collectors.toList());
            UserEntity user = userRepository.findByUsername(sve.getUsername());
            boolean hasUserVoted = user.getVotedPolls().containsKey(poll.getId());
            String votedOptionId = hasUserVoted ? user.getVotedPolls().get(poll.getId()).toHexString() : null;
            return new PollResponseDTO(poll, optionResponseDTOs, tagWithoutPollsDTOs, hasUserVoted, votedOptionId);
        }).collect(Collectors.toList());

        return pollResponseDTOs;
    }

    public List<PollResponseDTO> getSinglePoll(String sessionKey, SessionValueEntity sve, String pollId) {
        // Validate poll ID
        if (pollId == null || pollId.isEmpty()) {
            throw new IllegalArgumentException("Poll ID cannot be null or empty");
        }

        // Fetch poll by ID
        PollEntity poll = pollRepository.findById(new ObjectId(pollId)).orElseThrow(() -> new ResourceNotFoundException("Poll not found for the given ID: " + pollId));

        // Fetch the user
        UserEntity user = userRepository.findByUsername(sve.getUsername());
        if (user == null) {
            throw new ResourceNotFoundException("User not found for session");
        }

        // Check if the user has voted for this poll
        boolean hasUserVoted = user.getVotedPolls().containsKey(poll.getId());
        String optionIdVotedFor = hasUserVoted ? user.getVotedPolls().get(poll.getId()).toHexString() : null;

        // Map options to DTOs
        List<OptionResponseDTO> optionResponseDTOs = poll.getOptions().stream().map(optionId -> optionRepository.findByIdWithoutVotes(optionId)).filter(Optional::isPresent).map(Optional::get).map(option -> new OptionResponseDTO(option.getId(), option.getOption(), option.getVoteCount())).collect(Collectors.toList());

        // Map tags to DTOs
        List<TagWithoutPollsDTO> tagWithoutPollsDTOs = tagRepository.findAllByIdWithoutPollIdsIn(poll.getTags()).stream().map(tag -> new TagWithoutPollsDTO(tag.getId().toHexString(), tag.getName())).collect(Collectors.toList());

        // Create and return a single-element list containing PollResponseDTO
        PollResponseDTO pollResponseDTO = new PollResponseDTO(poll, optionResponseDTOs, tagWithoutPollsDTOs, hasUserVoted, optionIdVotedFor);

        return Collections.singletonList(pollResponseDTO); // Return single-element list
    }

}