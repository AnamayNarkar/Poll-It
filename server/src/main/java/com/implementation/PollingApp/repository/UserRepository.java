package com.implementation.PollingApp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.implementation.PollingApp.entity.UserEntity;

public interface UserRepository extends MongoRepository<UserEntity, ObjectId> {
        UserEntity findByUsername(String username);
}
