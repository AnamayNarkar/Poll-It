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
@Document(collection = "votes")
public class VoteEntity {

    @Id
    private ObjectId id;

    private ObjectId optionId;

    private String username;

    private Date creationDateTime;

    public VoteEntity(ObjectId optionId, String username) {
        this.id = new ObjectId();
        this.optionId = optionId;
        this.username = username;
        this.creationDateTime = new Date();
    }

}