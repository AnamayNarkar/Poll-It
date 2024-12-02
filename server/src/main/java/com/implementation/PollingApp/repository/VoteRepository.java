package com.implementation.PollingApp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.implementation.PollingApp.entity.VoteEntity;

public interface VoteRepository extends MongoRepository<VoteEntity, ObjectId> {

}
