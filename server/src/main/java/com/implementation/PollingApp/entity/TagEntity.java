package com.implementation.PollingApp.entity;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Vector;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tags")
public class TagEntity {

    private ObjectId id;

    private String name;

    private List<ObjectId> pollIds;

    public TagEntity(String name) {
        this.id = new ObjectId();
        this.name = name;
        this.pollIds = new Vector<>();
    }
}
