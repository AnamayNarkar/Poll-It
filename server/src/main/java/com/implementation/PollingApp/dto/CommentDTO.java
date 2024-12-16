package com.implementation.PollingApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    String id;
    String pollId;
    String username;
    String profilePictureURL;
    String comment;

}
