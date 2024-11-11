package com.implementation.JournalApp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.implementation.JournalApp.entity.UserEntity;

public interface UserRepository extends MongoRepository<UserEntity, ObjectId> {
        UserEntity findByUsername(String username);
}
