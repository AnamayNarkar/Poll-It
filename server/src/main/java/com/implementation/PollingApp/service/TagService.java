package com.implementation.PollingApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.implementation.PollingApp.dao.TagWithoutPollsDAO;
import com.implementation.PollingApp.dto.TagWithoutPollsDTO;
import com.implementation.PollingApp.entity.FeedValueEntity;
import com.implementation.PollingApp.entity.SessionValueEntity;
import com.implementation.PollingApp.entity.TagEntity;
import com.implementation.PollingApp.entity.UserEntity;
import com.implementation.PollingApp.exception.custom.TagException;
import com.implementation.PollingApp.repository.RedisRepositoryForFeed;
import com.implementation.PollingApp.repository.TagRepository;
import com.implementation.PollingApp.repository.UserRepository;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisRepositoryForFeed redisRepositoryForFeed;

    public TagWithoutPollsDTO createTag(String name) {
        TagWithoutPollsDAO tag = tagRepository.findTagByNameWithoutPollIds(name);
        if (tag != null) {
            throw new TagException("Tag with name '" + name + "' already exists");
        }
        TagEntity tagEntity = new TagEntity(name);
        tagRepository.save(tagEntity);
        return new TagWithoutPollsDTO(tagEntity.getId().toHexString(), name);
    }

    public TagWithoutPollsDTO followOrUnfollowTag(SessionValueEntity sessionValueEntity, String name) {
        String username = sessionValueEntity.getUsername();

        TagWithoutPollsDAO tag = tagRepository.findTagByNameWithoutPollIds(name);
        if (tag == null) {
            throw new TagException("Tag with name '" + name + "' does not exist");
        }

        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            if (userEntity.getFollowedTags().contains(tag.getId())) {
                userEntity.getFollowedTags().remove(tag.getId());
            } else {
                userEntity.getFollowedTags().add(tag.getId());
            }
            userRepository.save(userEntity);
        } else {
            throw new TagException("User with username '" + username + "' does not exist");
        }

        String feedStateRedisKey = sessionValueEntity.getFeedStateRedisKey();
        if (feedStateRedisKey != null) {
            FeedValueEntity feedValueEntity = redisRepositoryForFeed.getValue(feedStateRedisKey);
            if (feedValueEntity != null) {
                String tagIdString = tag.getId().toHexString();
                if (feedValueEntity.getFollowedTags().contains(tagIdString)) {
                    feedValueEntity.getFollowedTags().remove(tagIdString);
                    feedValueEntity.getFeedState().remove(tagIdString);
                } else {
                    feedValueEntity.getFollowedTags().add(tagIdString);
                    feedValueEntity.getFeedState().put(tagIdString, null);
                }
                redisRepositoryForFeed.updateValue(feedStateRedisKey, feedValueEntity);
            }
        }

        return new TagWithoutPollsDTO(tag.getId().toHexString(), tag.getName());
    }

}