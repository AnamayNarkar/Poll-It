package com.implementation.PollingApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.implementation.PollingApp.dao.TagWithoutPollsDAO;
import com.implementation.PollingApp.dto.TagWithoutPollsDTO;
import com.implementation.PollingApp.entity.TagEntity;
import com.implementation.PollingApp.exception.custom.TagException;
import com.implementation.PollingApp.repository.TagRepository;

@Service
public class TagService {

        @Autowired
        private TagRepository tagRepository;

        public TagWithoutPollsDTO createTag(String name) {
                TagWithoutPollsDAO tag = tagRepository.findTagByNameWithoutPollIds(name);
                if (tag != null) {
                        throw new TagException("Tag with name '" + name + "' already exists");
                }
                TagEntity tagEntity = new TagEntity(name);
                tagRepository.save(tagEntity);
                return new TagWithoutPollsDTO(tagEntity.getId().toHexString(), name);
        }
}