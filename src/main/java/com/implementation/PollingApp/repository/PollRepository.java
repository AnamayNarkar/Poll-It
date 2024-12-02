package com.implementation.PollingApp.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.implementation.PollingApp.entity.PollEntity;

public interface PollRepository extends MongoRepository<PollEntity, ObjectId> {

        PollEntity findByQuestion(String question);

        List<PollEntity> findByCreatedBy(String createdBy);

        @Query(value = "{ 'id' : ?0 }", fields = "{ question: 1, createdBy: 1, creationDateTime: 1, expirationDateTime: 1, options: 1 }")
        PollEntity findPollWithOptions(ObjectId id);

        @Query(value = "{ 'id' : { $in: ?0 } }", fields = "{ question: 1, createdBy: 1, creationDateTime: 1, expirationDateTime: 1, options: 1 }")
        List<PollEntity> findMultiplePollsWithOptions(List<ObjectId> ids);

        @Query(value = "{}", fields = "{ question: 1, createdBy: 1, creationDateTime: 1, expirationDateTime: 1, options: 1 }")
        List<PollEntity> findAllPollsWithOptions();
}