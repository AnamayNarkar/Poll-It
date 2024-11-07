package com.implementation.JournalApp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.implementation.JournalApp.entity.UserEntity;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, ObjectId> {
        UserEntity findByusername(String username);
}
