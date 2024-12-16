package com.implementation.PollingApp.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.implementation.PollingApp.entity.CommentEntity;

public interface CommentRepository extends MongoRepository<CommentEntity, ObjectId> {

    @Query(value = "{ 'pollId': ?0 }", sort = "{ 'creationDate': 1 }")
    List<CommentEntity> findAllByPollId(ObjectId pollId, Pageable pageable);
}
