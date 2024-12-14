package com.implementation.PollingApp.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.implementation.PollingApp.dto.UserForDisplayingSearchResultsDTO;
import com.implementation.PollingApp.entity.UserEntity;

public interface UserRepository extends MongoRepository<UserEntity, ObjectId> {
    UserEntity findByUsername(String username);

    UserEntity findByEmail(String email);

    @Query(value = "{ 'username': { $regex: ?0, $options: 'i' } }", fields = "{ 'username': 1 }")
    List<UserForDisplayingSearchResultsDTO> findByNameLike(String name, Pageable pageable);

}
