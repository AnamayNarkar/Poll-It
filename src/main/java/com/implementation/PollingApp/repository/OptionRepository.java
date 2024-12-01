package com.implementation.PollingApp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.implementation.PollingApp.entity.OptionEntity;

public interface OptionRepository extends MongoRepository<OptionEntity, ObjectId> {

}
