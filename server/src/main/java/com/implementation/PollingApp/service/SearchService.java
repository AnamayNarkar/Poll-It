package com.implementation.PollingApp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.implementation.PollingApp.dto.TagWithouPollsDTO;
import com.implementation.PollingApp.dto.UserForDisplayingSearchResultsDTO;
import com.implementation.PollingApp.repository.TagRepository;
import com.implementation.PollingApp.repository.UserRepository;

@Service
public class SearchService {

        @Autowired
        private TagRepository tagRepository;

        @Autowired
        private UserRepository userRepository;

        public List<TagWithouPollsDTO> getTagsLike(String searchString) {
                String regex = "^" + searchString;

                List<TagWithouPollsDTO> tags = tagRepository.findByNameLike(regex, PageRequest.of(0, 5));

                return tags.stream().sorted((t1, t2) -> Integer.compare(t1.getName().length(), t2.getName().length())).collect(Collectors.toList());
        }

        public List<UserForDisplayingSearchResultsDTO> getUsersLike(String searchString) {
                String regex = "^" + searchString;

                List<UserForDisplayingSearchResultsDTO> users = userRepository.findByNameLike(regex, PageRequest.of(0, 5));

                return users.stream().sorted((u1, u2) -> Integer.compare(u1.getUsername().length(), u2.getUsername().length())).collect(Collectors.toList());
        }

}
