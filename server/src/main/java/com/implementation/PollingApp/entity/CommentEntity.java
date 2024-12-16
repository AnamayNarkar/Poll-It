package com.implementation.PollingApp.entity;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comments")
public class CommentEntity {
    @Id
    private ObjectId id;
    private ObjectId pollId;
    private String username;
    private String comment;
    private Date creationDate;

    public CommentEntity(String pollId, String username, String comment) {
        this.id = new ObjectId();
        this.pollId = new ObjectId(pollId);
        this.username = username;
        this.comment = comment;
        this.creationDate = new Date();
    }

}
