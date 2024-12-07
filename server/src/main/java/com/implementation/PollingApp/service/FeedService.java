package com.implementation.PollingApp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

        public UserDataWithFollowedTagsDTO getUserData(SessionValueEntity sve) {
                UserEntity user = userRepository.findByUsername(sve.getUsername());
                List<TagWithoutPollsDAO> tagWithouPollsDAO = tagRepository.findAllByIdWithoutPollIdsIn(user.getFollowedTags());
                List<TagWithoutPollsDTO> tagsWithStringIds = tagWithouPollsDAO.stream().map(tag -> new TagWithoutPollsDTO(tag.getId().toHexString(), tag.getName())).collect(Collectors.toList());
                return new UserDataWithFollowedTagsDTO(user.getId().toHexString(), user.getUsername(), tagsWithStringIds);
        }

        public List<PollResponseDTO> getHomeFeed(String sessionKey, SessionValueEntity sve, Integer limit) {
                Boolean hasFeedState = sve.getFeedStateRedisKey() != null;
                FeedValueEntity feedValueEntity = null;

                if (hasFeedState) {
                        feedValueEntity = redisRepositoryForFeed.getValue(sve.getFeedStateRedisKey());
                }

                Boolean isFeedStateValid = true;

                if (feedValueEntity == null) {
                        isFeedStateValid = false;
                        feedValueEntity = new FeedValueEntity(new Vector<>(), new HashMap<>());
                        UserEntity user = userRepository.findByUsername(sve.getUsername());
                        feedValueEntity.setFollowedTags(user.getFollowedTags().stream().map(ObjectId::toHexString).collect(Collectors.toCollection(Vector::new)));
                        for (String tag : feedValueEntity.getFollowedTags()) {
                                feedValueEntity.getFeedState().put(tag, null);
                        }
                }

                if (feedValueEntity.getFollowedTags().isEmpty()) {
                        throw new ResourceNotFoundException("No tags followed by user");
                }

                Integer pollsAdded = 0;
                List<PollResponseDTO> homeFeed = new ArrayList<>();
                Integer followedTagsPointer = 0;
                Boolean hasFoundSomethingInThisIteration;

                while (pollsAdded < limit) {
                        hasFoundSomethingInThisIteration = false;

                        for (int i = 0; i < feedValueEntity.getFollowedTags().size(); i++) {
                                List<PollEntity> listOPollEntities;
                                ObjectId tag = new ObjectId(feedValueEntity.getFollowedTags().get(followedTagsPointer));

                                if (feedValueEntity.getFeedState().get(tag.toHexString()) != null) {
                                        listOPollEntities = pollRepository.findImmediatePollBefore(tag, new ObjectId(feedValueEntity.getFeedState().get(tag.toHexString())), PageRequest.of(0, 1));
                                } else {
                                        listOPollEntities = pollRepository.findLatestPollForThisTag(tag, PageRequest.of(0, 1));
                                }

                                if (listOPollEntities.size() > 0) {

                                        PollEntity poll = listOPollEntities.get(0);

                                        if (feedValueEntity.getFeedState().get(tag.toHexString()) != null && feedValueEntity.getFeedState().values().contains(poll.getId().toHexString())) {
                                                continue;
                                        }

                                        List<OptionResponseDTO> optionResponseDTOList = poll.getOptions().stream().map(optionId -> optionRepository.findByIdWithoutVotes(optionId)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
                                        homeFeed.add(new PollResponseDTO(poll, optionResponseDTOList));
                                        feedValueEntity.getFeedState().put(tag.toHexString(), poll.getId().toHexString());
                                        pollsAdded++;
                                        hasFoundSomethingInThisIteration = true;
                                }

                                followedTagsPointer = (followedTagsPointer + 1) % feedValueEntity.getFollowedTags().size();
                        }

                        if (!hasFoundSomethingInThisIteration) {
                                break;
                        }
                }

                if (pollsAdded == 0) {
                        homeFeed.clear();
                }

                if (hasFeedState && isFeedStateValid) {
                        redisRepositoryForFeed.updateValue(sve.getFeedStateRedisKey(), feedValueEntity);
                } else {
                        String redisKey = redisRepositoryForFeed.saveValue(feedValueEntity, 30);
                        sve = new SessionValueEntity(sve.getId(), sve.getUsername(), sve.getRoles(), redisKey);
                        redisRepositoryForSessions.updateValue(sessionKey, sve);
                }

                return homeFeed;
        }

}
