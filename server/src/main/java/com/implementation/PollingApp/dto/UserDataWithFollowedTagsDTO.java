package com.implementation.PollingApp.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDataWithFollowedTagsDTO {

        private String id;
        private String username;
        private List<TagWithoutPollsDTO> followedTags;
        private String profilePictureURL;

}
