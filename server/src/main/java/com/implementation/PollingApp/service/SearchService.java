package com.implementation.PollingApp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.implementation.PollingApp.dto.TagWithouPollsDTO;
import com.implementation.PollingApp.repository.TagRepository;

@Service
public class SearchService {

        @Autowired
        private TagRepository tagRepository;

        public List<TagWithouPollsDTO> getTagsLike(String searchString) {
                String regex = "^" + searchString;

                List<TagWithouPollsDTO> tags = tagRepository.findByNameLike(regex, PageRequest.of(0, 5));

                return tags.stream().sorted((t1, t2) -> Integer.compare(t1.getName().length(), t2.getName().length())).collect(Collectors.toList());
        }

}
