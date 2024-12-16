package com.implementation.PollingApp.entity;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comments")
public class CommentEntity {

    private ObjectId id;
    private ObjectId pollId;
    private String username;
    private String comment;

    public CommentEntity(String pollId, String username, String comment) {
        this.id = new ObjectId();
        this.pollId = new ObjectId(pollId);
        this.username = username;
        this.comment = comment;
    }

}
