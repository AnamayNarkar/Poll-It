package com.implementation.PollingApp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.implementation.PollingApp.dto.TagWithouPollsDTO;
import com.implementation.PollingApp.entity.TagEntity;
import com.implementation.PollingApp.exception.custom.TagError;
import com.implementation.PollingApp.repository.TagRepository;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public TagWithouPollsDTO createTag(String name) {
        TagWithouPollsDTO tag = tagRepository.findTagByNameWithoutPollIds(name);
        if (tag != null) {
            throw new TagError("Tag with name " + name + " already exists");
        }
        TagEntity tagEntity = new TagEntity(name);
        tagRepository.save(tagEntity);
        return new TagWithouPollsDTO(tagEntity.getId().toHexString(), name);
    }
}