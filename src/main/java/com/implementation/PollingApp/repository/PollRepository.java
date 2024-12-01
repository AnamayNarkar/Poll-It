package com.implementation.PollingApp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.implementation.PollingApp.entity.PollEntity;

public interface PollRepository extends MongoRepository<PollEntity, ObjectId> {

        PollEntity findByQuestion(String question);

}