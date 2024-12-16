package com.implementation.PollingApp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.implementation.PollingApp.entity.CommentEntity;

public interface CommentRepository extends MongoRepository<CommentEntity, ObjectId> {

}
