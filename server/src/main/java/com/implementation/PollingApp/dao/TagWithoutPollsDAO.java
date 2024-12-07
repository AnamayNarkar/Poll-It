package com.implementation.PollingApp.dao;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagWithoutPollsDAO {
    private ObjectId id;
    private String name;
}
