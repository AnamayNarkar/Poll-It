package com.implementation.PollingApp.service;

import java.util.ArrayList;
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
import org.springframework.stereotype.Service;

import com.implementation.PollingApp.dao.TagWithoutPollsDAO;
import com.implementation.PollingApp.dto.OptionResponseDTO;
import com.implementation.PollingApp.dto.PollResponseDTO;
import com.implementation.PollingApp.dto.TagWithoutPollsDTO;
import com.implementation.PollingApp.dto.UserDataWithFollowedTagsDTO;
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
                Boolean hasFeedState = sve.getFeedStateRedisKey() != null;
                FeedValueEntity feedValueEntity = null;

                if (hasFeedState) {
                        feedValueEntity = redisRepositoryForFeed.getValue(sve.getFeedStateRedisKey());
                }

                UserEntity user = userRepository.findByUsername(sve.getUsername());

                if (feedValueEntity == null) {
                        feedValueEntity = new FeedValueEntity(new Vector<>(), new HashMap<>(), new HashSet<>());
                        feedValueEntity.setFollowedTags(user.getFollowedTags().stream().map(ObjectId::toHexString).collect(Collectors.toList()));
                        for (String tag : feedValueEntity.getFollowedTags()) {
                                feedValueEntity.getFeedState().put(tag, null);
                        }
                        feedValueEntity.setSentPostIds(new HashSet<>());
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

                                System.out.println("Current feed state for " + tagRepository.findById(tag).get().getName() + ": " + feedValueEntity.getFeedState().get(tagHex));

                                List<PollEntity> listOfPolls;
                                if (feedValueEntity.getFeedState().get(tagHex) != null) {
                                        listOfPolls = pollRepository.findImmediatePollBefore(tag, new ObjectId(feedValueEntity.getFeedState().get(tagHex)), PageRequest.of(0, 1));
                                } else {
                                        listOfPolls = pollRepository.findLatestPollForThisTag(tag, PageRequest.of(0, 1));
                                }

                                if (!listOfPolls.isEmpty()) {
                                        PollEntity poll = listOfPolls.get(0);
                                        String pollId = poll.getId().toHexString();

                                        System.out.println("Processing tag: " + tagRepository.findById(tag).get().getName());
                                        System.out.println("Found poll: " + poll.getQuestion());
                                        System.out.println("Poll ID: " + pollId);
                                        System.out.println("Already processed: " + processedPollIds.contains(pollId));

                                        if (!feedValueEntity.getSentPostIds().contains(pollId)) {

                                                if (!processedPollIds.contains(pollId) && !user.getVotedPolls().containsKey(poll.getId())) {
                                                        processedPollIds.add(pollId);

                                                        feedValueEntity.getFeedState().put(tagHex, pollId);
                                                        System.out.println("Updated feed state for " + tagRepository.findById(tag).get().getName() + " to: " + pollId);

                                                        List<OptionResponseDTO> optionResponseDTOs = poll.getOptions().stream().map(optionId -> optionRepository.findByIdWithoutVotes(optionId)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());

                                                        List<TagWithoutPollsDAO> tagWithoutPollsDAOs = tagRepository.findAllByIdWithoutPollIdsIn(poll.getTags());
                                                        List<TagWithoutPollsDTO> tagWithoutPollsDTOs = tagWithoutPollsDAOs.stream().map(tagDAO -> new TagWithoutPollsDTO(tagDAO.getId().toHexString(), tagDAO.getName())).collect(Collectors.toList());

                                                        homeFeed.add(new PollResponseDTO(poll, optionResponseDTOs, tagWithoutPollsDTOs));
                                                        feedValueEntity.getSentPostIds().add(pollId);

                                                        pollsAdded++;
                                                        hasFoundSomethingInThisIteration = true;
                                                }
                                        } else {
                                                feedValueEntity.getFeedState().put(tagHex, pollId);
                                        }
                                }

                                followedTagsPointer = (followedTagsPointer + 1) % feedValueEntity.getFollowedTags().size();
                                if (pollsAdded >= limit)
                                        break;
                        }

                        if (!hasFoundSomethingInThisIteration)
                                break;
                }

                if (hasFeedState) {
                        redisRepositoryForFeed.updateValue(sve.getFeedStateRedisKey(), feedValueEntity);
                } else {
                        String redisKey = redisRepositoryForFeed.saveValue(feedValueEntity, 30);
                        sve = new SessionValueEntity(sve.getId(), sve.getUsername(), sve.getRoles(), redisKey);
                        redisRepositoryForSessions.updateValue(sessionKey, sve);
                }

                return homeFeed;
        }

        public UserDataWithFollowedTagsDTO getUserData(SessionValueEntity sve) {
                UserEntity user = userRepository.findByUsername(sve.getUsername());
                List<TagWithoutPollsDAO> tagWithouPollsDAO = tagRepository.findAllByIdWithoutPollIdsIn(user.getFollowedTags());
                List<TagWithoutPollsDTO> tagsWithStringIds = tagWithouPollsDAO.stream().map(tag -> new TagWithoutPollsDTO(tag.getId().toHexString(), tag.getName())).collect(Collectors.toList());
                return new UserDataWithFollowedTagsDTO(user.getId().toHexString(), user.getUsername(), tagsWithStringIds);
        }
}